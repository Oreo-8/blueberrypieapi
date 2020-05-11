package com.example.blueberrypieapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blueberrypieapi.config.JwtConfig;
import com.example.blueberrypieapi.config.MailConfig;
import com.example.blueberrypieapi.config.RabbitMqConfig;
import com.example.blueberrypieapi.entity.*;
import com.example.blueberrypieapi.service.LoginLogService;
import com.example.blueberrypieapi.service.RoleService;
import com.example.blueberrypieapi.service.UserRoleService;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.utils.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JwtConfig jwtConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final UserService userService;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    private final LoginLogService loginLogService;

    private final RequestUtil requestUtil;

    private HttpServletRequest request;

    private BCryptPasswordEncoder encoder;

    private JwtTokenUtil jwtTokenUtil;

    private RabbitTemplate rabbitTemplate;

    private RandomUtil randomUtil;

    /**
     * 根据用户名查询用户
     *
     * @param name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = this.findUserByName(name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        List<Role> roles = roleService.findUserRoles(user.getUserId());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), authorities);
    }

    /**
     * 登录
     * 返回token，用户名，用户角色
     *
     * @param user
     * @return
     */
    public Map login(User user) throws RuntimeException {
        User dbUser = this.findUserByName(user.getUserName());

        //此用户不存在 或 密码错误
        if (null == dbUser || !encoder.matches(user.getUserPassword(), dbUser.getUserPassword())) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //用户已被封禁
        if (0 == dbUser.getUserState()) {
            throw new RuntimeException("你已被封禁");
        }

        //用户名 密码 匹配 签发token
        final UserDetails userDetails = this.loadUserByUsername(user.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        Map<String, Object> map = new HashMap<>(4);

        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", user.getUserName());
        map.put("roles", roles);
        map.put("img",dbUser.getImagePath());
        //将token存入redis 过期时间 jwtConfig.time 单位[s]
        redisTemplate.opsForValue().
                set(JwtConfig.REDIS_TOKEN_KEY_PREFIX + user.getUserName(),
                        jwtConfig.getPrefix() + token, jwtConfig.getTime(), TimeUnit.SECONDS);

        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(requestUtil.getIpAddress(request));
        loginLog.setLoginTime(LocalDateTime.now());
        loginLog.setUserId(dbUser.getUserId());
        loginLogService.save(loginLog);

        return map;
    }

    /**
     * 根据用户名查询用户
     *
     * @param name
     * @return
     */
    public User findUserByName(String name) {
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName,name));
    }

    /**
     * 从token中提取信息
     *
     * @param authHeader
     * @return
     */
    public UserDetails loadUserByToken(String authHeader) {
        final String authToken = authHeader.substring(jwtConfig.getPrefix().length());//除去前缀，获取token

        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if (null == username) {
            return null;
        }

        String redisToken = redisTemplate.opsForValue().get(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
        //从redis中取不到值 或 值 不匹配
        if (!authHeader.equals(redisToken)) {
            return null;
        }
        User user = new User();
        user.setUserName(username);

        List<String> roles = jwtTokenUtil.getRolesFromToken(authToken);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), "***********", authorities);
    }

    /**
     * 注册
     *
     * @param
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(User user,String mailCode){
        String code = redisTemplate.opsForValue().get(MailConfig.REDIS_MAIL_KEY_PREFIX + user.getUserMail());
        if (!mailCode.equals(code)){
            throw new RuntimeException("验证码错误");
        }
        User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, user.getUserName())
                .or().eq(User::getUserMail,user.getUserMail()));
        if (one != null){
            throw new RuntimeException("用户名或邮箱已存在");
        }

        user.setUserPassword(encoder.encode(user.getUserPassword()));
        user.setUserState(1);
        user.setDeleteState(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        List<Role> roles = new ArrayList<>(1);
        roles.add(roleService.getOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName,"USER")));
        user.setRoles(roles);
        userService.save(user);

        roles.forEach(x-> userRoleService.save(UserRole.builder().userId(user.getUserId()).roleId(x.getRoleId()).build()));
    }

    /**
     * 退出登录
     * 删除redis中的key
     */
    public void logout() {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        redisTemplate.delete(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
    }

    /**
     * 将 邮件 和 验证码发送到消息队列
     *
     * @param mail
     */
    public void sendMail(String mail) {
        //貌似线程不安全 范围100000 - 999999
        Integer random = randomUtil.nextInt(100000, 999999);
        Map<String, String> map = new HashMap<>();
        String code = random.toString();
        map.put("mail", mail);
        map.put("code", code);

        //保存发送记录
        redisTemplate.opsForValue()
                .set(MailConfig.REDIS_MAIL_KEY_PREFIX + mail, code, MailConfig.EXPIRED_TIME, TimeUnit.MINUTES);

        rabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_EXCHANGE,RabbitMqConfig.MAIL_QUEUE_KEY, map);
    }

    /**
     * 生成 base64 验证码图片
     * @return
     */
    public ImgResult imageCode(){
        // 生成随机字串
        String verifyCode = CodeUtils.generateVerifyCode(4);
        String uuid = UUID.randomUUID().toString();
        // 存入redis
        redisTemplate.opsForValue().set(jwtConfig.getLogin()+uuid,verifyCode, jwtConfig.getLogin_time(), TimeUnit.MINUTES);
        int w = 111, h = 35;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()){
            CodeUtils.outputImage(w, h, stream, verifyCode);
            return new ImgResult("data:image/png;base64,"+ Base64.encode(stream.toByteArray()),uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断验证码
     * @param oid
     * @param imgCode
     * @return
     */
    public boolean isImageCode(String oid, String imgCode){
        String code = redisTemplate.opsForValue().get(jwtConfig.getLogin() + oid);
        if (code == null){
            return false;
        }
        return code.equals(imgCode.toUpperCase());
    }

    public R upUserDate(String name,HttpServletRequest request){
        String uName = jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getTokenFromRequest(request));
        List<String> roles = jwtTokenUtil.getRolesFromToken(jwtTokenUtil.getTokenFromRequest(request));
        if (!name.equals(uName) && !roles.contains("ROLE_ADMIN")){
            return R.failed("权限不足");
        }
        return R.ok("修改成功");
    }
}

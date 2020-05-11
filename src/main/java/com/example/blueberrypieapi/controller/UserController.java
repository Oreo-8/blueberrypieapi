package com.example.blueberrypieapi.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.config.MailConfig;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.entity.UserRole;
import com.example.blueberrypieapi.service.UserRoleService;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.service.serviceImpl.UserDetailsServiceImpl;
import com.example.blueberrypieapi.utils.FormatUtil;
import com.example.blueberrypieapi.utils.JwtTokenUtil;
import com.example.blueberrypieapi.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Api(tags = "用户api",value = "用户api")
public class UserController {

    private final FormatUtil formatUtil;

    private final UserService service;

    private final UserDetailsServiceImpl userService;

    private final RedisTemplate<String, String> redisTemplate;

    private final HttpServletRequest request;

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 登录返回token
     * @param name
     * @param password
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
    @GetMapping("/login")
    public R login(String name, String password, String oid, String imgCode) {
        if (!formatUtil.checkStringNull(name, password,oid,imgCode)) {
            return R.failed("参数错误");
        }
        if(!userService.isImageCode(oid,imgCode)){
            return R.failed("验证码不正确");
        }
        try {
            User user = new User();
            user.setUserName(name);
            user.setUserPassword(password);
            return R.ok(userService.login(user));
        } catch (UsernameNotFoundException unfe) {
            return R.failed("登录失败，用户名或密码错误");
        } catch (RuntimeException re) {
            return  R.failed(re.getMessage());
        }

    }

    /**
     * 用户退出登录
     * 删除redis中的token
     *
     * @param
     * @return
     */
    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout")
    public R logout() {
        userService.logout();
        return R.ok("退出成功");
    }

    /**
     * 用户注册
     * @param user
     * @param mailCode
     * @return
     */
    @GetMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱+邮箱验证码")
    public R register(User user,String mailCode){
        if (!formatUtil.checkStringNull(user.getUserName(),user.getUserPassword(),user.getUserMail(),mailCode)){
            return R.failed("注册失败，字段不完整");
        }
        try {
            userService.register(user, mailCode);
            return R.ok("注册成功");
        } catch (RuntimeException e) {
            return R.failed("注册失败，" + e.getMessage());
        }
    }

    /**
     * 发送验证邮件
     * 异步发送
     *
     * @param mail
     * @return
     */
    @ApiOperation(value = "发送验证邮件", notes = "mail 1 冷却分钟")
    @GetMapping("/sendMail")
    public R sendMail(String mail) {
        //邮箱格式校验
        if (!(formatUtil.checkStringNull(mail)) || (!formatUtil.checkMail(mail))) {
            return R.failed("邮箱格式错误");
        }
        String redisMailCode = redisTemplate.opsForValue().get(MailConfig.REDIS_MAIL_KEY_PREFIX + mail);
        //此邮箱发送过验证码
        if (redisMailCode != null) {

            return R.failed(MailConfig.EXPIRED_TIME + "分钟内不可重发验证码");
        } else {
            userService.sendMail(mail);
            return R.ok("发送成功");
        }
    }

    /**
     * 发送验证码图片
     * @return
     */
    @ApiOperation(value = "发送验证码图片", notes = "发送验证码图片")
    @GetMapping("/code")
    public R getCode(){
        return R.ok(userService.imageCode());
    }

//    @GetMapping("/AllUser")
//    @ApiOperation(value = "查看所有用户 (管理员能访问)", notes = "查看所有用户 (管理员能访问)")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public R getAllUser(Page page){
//        return R.ok(service.page(page));
//    }

    /**
     * 修改头像
     * @return
     */
    @PostMapping("/setImage")
    public R setImage(MultipartFile file){
        if (file == null) {
            return R.failed("未选择图片");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D://ChangZhi2//"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap(1);
        String http = request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() + "/images/"+fileName;
        map.put("path",http);
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        User user = new User();
        user.setImagePath(http);
        service.update(user,Wrappers.<User>lambdaQuery().eq(User::getUserName,userName));
        return R.ok(map);
    }

    @GetMapping("/getUserDtl/{name}")
    public R getUserDtl(@PathVariable("name")String name){
        return R.ok(service.getUserDtl(name));
    }

    @PutMapping("/upUserData")
    public R upUserData(@RequestBody User user){
        user.setUserName(null);
        user.setUserPassword(null);
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(null);
        user.setUserMail(null);
        user.setImagePath(null);
        user.setDeleteState(null);
        user.setUserState(null);
        return R.ok(service.update(user,Wrappers.<User>lambdaQuery()
                .eq(User::getUserName,jwtTokenUtil.getUsernameFromRequest(request))));
    }

}


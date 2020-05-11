package com.example.blueberrypieapi.service.serviceImpl;

import com.example.blueberrypieapi.entity.Reply;
import com.example.blueberrypieapi.mapper.ReplyMapper;
import com.example.blueberrypieapi.service.BlogService;
import com.example.blueberrypieapi.service.ReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@Service
@AllArgsConstructor
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    private final BlogService blogService;

    @Override
    public boolean sendReply(Reply reply, HttpServletRequest request) {
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        reply.setFromUserId(userService.getIdByName(userName));
        reply.setDeleteState(1);
        reply.setCreateTime(LocalDateTime.now());
        blogService.addDiscussCount(reply.getBlogId());
        return this.save(reply);
    }
}

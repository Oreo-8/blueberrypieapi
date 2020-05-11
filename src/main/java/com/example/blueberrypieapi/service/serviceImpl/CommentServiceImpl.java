package com.example.blueberrypieapi.service.serviceImpl;

import com.example.blueberrypieapi.entity.Comment;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.mapper.CommentMapper;
import com.example.blueberrypieapi.service.BlogService;
import com.example.blueberrypieapi.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    private final BlogService blogService;

    private final CommentMapper commentMapper;

    @Override
    public boolean sendComment(Comment comment, HttpServletRequest request){
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        comment.setCreateTime(LocalDateTime.now());
        comment.setDeleteState(1);
        comment.setUserId(userService.getIdByName(userName));
        blogService.addDiscussCount(comment.getBlogId());
        return this.save(comment);
    }

    @Override
    public List<Comment> getComment(String id) {
        return commentMapper.getComment(id);
    }
}

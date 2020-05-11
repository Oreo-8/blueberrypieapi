package com.example.blueberrypieapi.service;

import com.example.blueberrypieapi.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
public interface CommentService extends IService<Comment> {

    boolean sendComment(Comment comment, HttpServletRequest request);

    List<Comment> getComment(String id);

}

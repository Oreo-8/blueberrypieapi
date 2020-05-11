package com.example.blueberrypieapi.service;

import com.example.blueberrypieapi.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
public interface ReplyService extends IService<Reply> {

    boolean sendReply(Reply reply, HttpServletRequest request);

}

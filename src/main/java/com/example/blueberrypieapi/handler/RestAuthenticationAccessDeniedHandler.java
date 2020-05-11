package com.example.blueberrypieapi.handler;

import com.alibaba.fastjson.JSON;
import com.example.blueberrypieapi.utils.R;
import com.example.blueberrypieapi.utils.StatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse response, AccessDeniedException e) throws IOException {
        //登陆状态下，权限不足执行该方法
        response.setStatus(StatusCode.ACCESSERROR);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = JSON.toJSONString(R.failed(StatusCode.ACCESSERROR, "用户权限不足"));
        printWriter.write(body);
        printWriter.flush();
    }
}

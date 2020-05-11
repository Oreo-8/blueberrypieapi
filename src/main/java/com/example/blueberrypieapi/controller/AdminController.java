package com.example.blueberrypieapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.utils.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminBlog")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Api(tags = "管理博客api",value = "管理博客api")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/page")
    public R getAllUserPage(Page page, User user){
        return R.ok(userService.getAllUserPage(page,user));
    }

}

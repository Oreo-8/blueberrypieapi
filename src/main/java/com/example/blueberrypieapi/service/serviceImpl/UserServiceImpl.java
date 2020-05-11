package com.example.blueberrypieapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.Role;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.entity.UserRole;
import com.example.blueberrypieapi.mapper.UserMapper;
import com.example.blueberrypieapi.service.RoleService;
import com.example.blueberrypieapi.service.UserRoleService;
import com.example.blueberrypieapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final UserRoleService userRoleService;

    private final RoleService roleService;

    @Override
    public Integer getIdByName(String name) {
        return userMapper.getIdByName(name);
    }

    @Override
    public User getUserDtl(String name) {

        User userDtl = userMapper.getUserDtl(name);
        List<UserRole> userRoles = userRoleService.list(Wrappers.<UserRole>lambdaQuery().
                eq(UserRole::getUserId, userDtl.getUserId()));
        List<String> collect = userRoles.stream().map(x ->
                roleService.getOne(Wrappers.<Role>lambdaQuery()
                        .eq(Role::getRoleId, x.getRoleId())).getRoleName())
                .collect(Collectors.toList());

        if (collect.contains("ADMIN")){
            userDtl.setIdentity("管理员");
        }else{
            userDtl.setIdentity("普通用户");
        }

        return userDtl;
    }

    @Override
    public IPage<User> getAllUserPage(Page page, User user) {
        return userMapper.getAllUserPage(page,user);
    }
}

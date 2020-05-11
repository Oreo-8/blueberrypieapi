package com.example.blueberrypieapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blueberrypieapi.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
public interface UserService extends IService<User> {

    Integer getIdByName(String name);

    User getUserDtl(String name);

    IPage<User> getAllUserPage(Page page,User user);

}

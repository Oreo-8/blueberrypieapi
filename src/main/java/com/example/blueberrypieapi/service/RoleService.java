package com.example.blueberrypieapi.service;

import com.example.blueberrypieapi.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询角色
     * @param id
     * @return
     */
    List<Role> findUserRoles(Integer id);
}

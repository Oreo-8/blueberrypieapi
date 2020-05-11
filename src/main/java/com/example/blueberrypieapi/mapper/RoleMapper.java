package com.example.blueberrypieapi.mapper;

import com.example.blueberrypieapi.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色
     * @param id
     * @return
     */
    List<Role> findUserRoles(Integer id);

}

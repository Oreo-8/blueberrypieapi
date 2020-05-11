package com.example.blueberrypieapi.service.serviceImpl;

import com.example.blueberrypieapi.entity.Role;
import com.example.blueberrypieapi.mapper.RoleMapper;
import com.example.blueberrypieapi.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<Role> findUserRoles(Integer id) {
        return roleMapper.findUserRoles(id);
    }
}

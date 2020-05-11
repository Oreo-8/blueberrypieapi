package com.example.blueberrypieapi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
public interface UserMapper extends BaseMapper<User> {

    Integer getIdByName(@Param("name")String name);

    User getUserDtl(@Param("name")String name);

    IPage<User> getAllUserPage(Page page, @Param("user")User user);

}

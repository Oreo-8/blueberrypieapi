package com.example.blueberrypieapi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
public interface BlogMapper extends BaseMapper<Blog> {

    IPage<Blog> getBlogPage(Page page, @Param("blog")Blog blog);

    Blog getBlogById(@Param("id")String id);

    boolean addDiscussCount(@Param("id")Integer id);

    boolean removeByUserName(@Param("id")String id,@Param("name")String name);

}

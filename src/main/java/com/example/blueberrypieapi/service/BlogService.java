package com.example.blueberrypieapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blueberrypieapi.entity.Blog;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
public interface BlogService extends IService<Blog> {

    boolean saveBlog(Blog blog, HttpServletRequest request);

    IPage<Blog> getBlogPage(Page page, Blog blog);

    IPage<Blog> getBlogByUser(Page page,HttpServletRequest request,Blog blog);

    Blog getBlogById(String id);

    boolean addDiscussCount(Integer id);

    boolean delete(String id,HttpServletRequest request);
}

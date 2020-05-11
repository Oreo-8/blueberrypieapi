package com.example.blueberrypieapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.Blog;
import com.example.blueberrypieapi.entity.BlogTag;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.mapper.BlogMapper;
import com.example.blueberrypieapi.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blueberrypieapi.service.BlogTagService;
import com.example.blueberrypieapi.service.UserService;
import com.example.blueberrypieapi.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@Service
@AllArgsConstructor
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    private final UserService userService;

    private final BlogTagService blogTagService;

    private final JwtTokenUtil jwtTokenUtil;

    private final BlogMapper blogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBlog(Blog blog, HttpServletRequest request){
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        blog.setUserId(userService.getIdByName(userName));
        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());
        blog.setDeleteState(1);
        boolean r = this.save(blog);
        blog.setBlogTags(blog.getBlogTags().stream().peek(x -> x.setBlogId(blog.getId())).collect(Collectors.toList()));
        blogTagService.saveBatch(blog.getBlogTags());
        return r;
    }

    @Override
    public IPage<Blog> getBlogPage(Page page, Blog blog) {
        return blogMapper.getBlogPage(page,blog);
    }

    @Override
    public IPage<Blog> getBlogByUser(Page page,HttpServletRequest request,Blog blog) {
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        blog.setUserId(userService.getIdByName(userName));
        return blogMapper.getBlogPage(page,blog);
    }

    @Override
    public Blog getBlogById(String id) {
        Blog r = blogMapper.getBlogById(id);
        r.setViews(r.getViews()+1);
        this.updateById(r);
        return r;
    }

    @Override
    public boolean addDiscussCount(Integer id) {
        return blogMapper.addDiscussCount(id);
    }

    @Override
    public boolean delete(String id,HttpServletRequest request) {
        if (jwtTokenUtil.isAdmin(request)){
            return this.removeById(id);
        }else {
            String userName = jwtTokenUtil.getUsernameFromRequest(request);
            return blogMapper.removeByUserName(id,userName);
        }
    }
}

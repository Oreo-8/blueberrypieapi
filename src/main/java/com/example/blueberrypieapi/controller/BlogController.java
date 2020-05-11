package com.example.blueberrypieapi.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blueberrypieapi.entity.Blog;
import com.example.blueberrypieapi.entity.Comment;
import com.example.blueberrypieapi.entity.Reply;
import com.example.blueberrypieapi.entity.User;
import com.example.blueberrypieapi.service.BlogService;
import com.example.blueberrypieapi.service.CommentService;
import com.example.blueberrypieapi.service.ReplyService;
import com.example.blueberrypieapi.service.TagService;
import com.example.blueberrypieapi.utils.JwtTokenUtil;
import com.example.blueberrypieapi.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@RestController
@RequestMapping("/blog")
@Api(tags = "博客api",value = "博客api")
@AllArgsConstructor
public class BlogController {

    private final TagService tagService;

    private final HttpServletRequest request;

    private final BlogService blogService;

    private final CommentService commentService;

    private final ReplyService replyService;

    @GetMapping("/getTag")
    @ApiOperation(value = "获取TAG", notes = "获取TAG")
    public R getTag(){
        return R.ok(tagService.list());
    }

    @PostMapping("/fileUpload")
    @ApiOperation(value = "发布博客图片接口", notes = "发布博客图片接口")
    public R fileUpload(MultipartFile file){
        if (file.isEmpty()) {
            return R.failed("文件为空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D://bodyImg//"+new SimpleDateFormat("yyyyMMdd").format(new Date()) +"//"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap(1);
        map.put("path",request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() + "/bodyImg/"+
                new SimpleDateFormat("yyyyMMdd").format(new Date())+"/"+fileName);
        return R.ok(map);
    }

    @PostMapping("/saveBlog")
    @ApiOperation(value = "保存博客", notes = "保存博客")
    public R saveBlog(@RequestBody Blog blog){
        return R.ok(blogService.saveBlog(blog,request));
    }

    @GetMapping("/getBlogPage")
    @ApiOperation(value = "获取分页博客信息", notes = "获取分页博客信息")
    public R getBlogPage(Page page,Blog blog){
        return R.ok(blogService.getBlogPage(page,blog));
    }

    @GetMapping("/getBlogByUser")
    @ApiOperation(value = "根据token获取博客", notes = "根据token获取博客")
    public R getBlogByUser(Page page,Blog blog){
        return R.ok(blogService.getBlogByUser(page,request,blog));
    }

    @GetMapping("/getBlogById/{id}")
    @ApiOperation(value = "根据id获取博客", notes = "根据id获取博客")
    public R getBlogById(@PathVariable("id") String id){
        return R.ok(blogService.getBlogById(id));
    }

    @PutMapping("/sendComment")
    @ApiOperation(value = "保存评论", notes = "保存评论")
    public R sendComment(Comment comment){
        return R.ok(commentService.sendComment(comment,request));
    }

    @GetMapping("/getComment/{id}")
    @ApiOperation(value = "获取评论列表", notes = "获取评论列表")
    public R getComment(@PathVariable("id") String id){
        return R.ok(commentService.getComment(id));
    }

    @PutMapping("/sendReply")
    @ApiOperation(value = "保存回复", notes = "保存回复")
    public R sendReply(Reply reply){
        return R.ok(replyService.sendReply(reply,request));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") String id){
        return R.ok(blogService.delete(id,request));
    }

}


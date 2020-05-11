package com.example.blueberrypieapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@Data
public class Blog implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String body;

    /**
     * 用户id
     */
    private Integer userId;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String imagePath;

    /**
     * 评论数
     */
    private Integer discussCount = 0;

    /**
     * 浏览数
     */
    private Integer views = 0;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<BlogTag> blogTags;

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private String tagId;

    /**
     * 0 删除 1正常
     */
    @TableLogic
    private Integer deleteState;
}

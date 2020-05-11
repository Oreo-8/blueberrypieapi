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
public class Comment implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博客id
     */
    private Integer blogId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 0 删除 1正常
     */
    @TableLogic
    private Integer deleteState;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String imagePath;

    @TableField(exist = false)
    private List<Reply> replies;
}

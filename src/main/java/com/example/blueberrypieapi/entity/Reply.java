package com.example.blueberrypieapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@Data
public class Reply implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *评论id
     */
    private Integer commentId;

    /**
     *回复内容
     */
    private String content;

    /**
     * 回复用户id
     */
    private Integer fromUserId;

    @TableField(exist = false)
    private String foUserName;

    @TableField(exist = false)
    private String foImagePath;

    /**
     *目标用户id
     */
    private Integer toUserId;

    @TableField(exist = false)
    private String toUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 0 删除 1正常
     */
    @TableLogic
    private Integer deleteState;

    @TableField(exist = false)
    private Integer blogId;

}

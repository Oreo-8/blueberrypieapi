package com.example.blueberrypieapi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author cheng
 * @since 2020-04-20
 */
@Data
public class User implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 用户唯一id--主键
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名--不能重复
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userMail;

    /**
     * 用户状态 0 封禁 1正常
     */
    private Integer userState;

    /**
     * 用户状态 0 删除 1正常
     */
    @TableLogic
    private Integer deleteState;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 头像路径
     */
    private String imagePath;

    /**
     * 简介
     */
    private String Introduction;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private Integer count;

    @TableField(exist = false)
    private String identity;

}

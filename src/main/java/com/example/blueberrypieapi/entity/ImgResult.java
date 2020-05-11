package com.example.blueberrypieapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 验证的id和图片的属性
 * */
@Data
@AllArgsConstructor
public class ImgResult {
    private String img;
    private String uuid;
}

/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.example.blueberrypieapi.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记")
	private int code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String msg;


	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, StatusCode.OK, null);
	}

	public static <T> R<T> ok(String msg) {
		return restResult(null, StatusCode.OK, msg);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, StatusCode.OK, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, StatusCode.OK, msg);
	}

	public static <T> R<T> ok(T data, int code, String msg) {
		return restResult(data, code, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, StatusCode.ERROR, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, StatusCode.ERROR, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, StatusCode.ERROR, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, StatusCode.ERROR, msg);
	}

	public static <T> R<T> failed(int code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> R<T> failed(T data, int code, String msg) {
		return restResult(data, code, msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}

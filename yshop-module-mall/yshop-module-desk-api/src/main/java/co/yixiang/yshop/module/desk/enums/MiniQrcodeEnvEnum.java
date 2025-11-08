/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.desk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author hupeng
 * 小程序二维码版本相关枚举
 */
@Getter
@AllArgsConstructor
public enum MiniQrcodeEnvEnum {

	RELEASE("release","正式版"),
	TRIAL("trial","体验版"),
	DEVELOP("develop","开发版");



	private String value;
	private String desc;



}

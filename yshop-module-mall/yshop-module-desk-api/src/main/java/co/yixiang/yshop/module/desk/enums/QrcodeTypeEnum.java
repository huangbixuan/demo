/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.desk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 二维码类型相关枚举
 */
@Getter
@AllArgsConstructor
public enum QrcodeTypeEnum {

	MINI_UNLIMIT("mini_unlimit","小程序无限制二维码"),
	NORMAL("normal","正常普通二维码");



	private String value;
	private String desc;



}

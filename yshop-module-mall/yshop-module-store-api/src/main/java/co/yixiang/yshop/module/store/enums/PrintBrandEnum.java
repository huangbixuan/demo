/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 打印机品牌枚举
 */
@Getter
@AllArgsConstructor
public enum PrintBrandEnum {

	YLY("yly","易联云打印机"),
	FEIE("feie","飞蛾打印机");



	private String value;
	private String desc;



}

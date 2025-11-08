/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.desk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 桌面相关枚举
 */
@Getter
@AllArgsConstructor
public enum DeskStatusEnum {

	DESK_ALL("all","所有"),
	DESK_EMPTY("empty","空闲桌面"),
	DESK_ING("ing","就餐中");



	private String value;
	private String desc;



}

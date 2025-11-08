/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author hupeng
 * 订单预约相关枚举
 */
@Getter
@AllArgsConstructor
public enum OrderDueStatusEnum {
	DUE_STATUS_9(9,"就餐中"),
	DUE_STATUS_1(1,"预约中"),
	DUE_STATUS_2(2,"取消预约"),
	DUE_STATUS_3(3,"已完成");



	private Integer value;
	private String desc;

	public static OrderDueStatusEnum toType(int value) {
		return Stream.of(OrderDueStatusEnum.values())
				.filter(p -> p.value == value)
				.findAny()
				.orElse(null);
	}


}

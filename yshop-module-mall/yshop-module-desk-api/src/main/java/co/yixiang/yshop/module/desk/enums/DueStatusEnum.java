package co.yixiang.yshop.module.desk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 时间规则枚举
 */
@Getter
@AllArgsConstructor
public enum DueStatusEnum {
    STATUS_9(9,"就餐中"),
    STATUS_0(0,"正常"),
    STATUS_1(1,"已被预约"),
    STATUS_2(2,"时间已过期");

    private Integer value;
    private String desc;
}

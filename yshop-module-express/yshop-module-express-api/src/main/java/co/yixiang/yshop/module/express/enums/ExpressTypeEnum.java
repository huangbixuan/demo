package co.yixiang.yshop.module.express.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 快递类型枚举
 */
@Getter
@AllArgsConstructor
public enum ExpressTypeEnum {
    TYPE_0(0,"普通快递"),
    TYPE_1(1,"同城快递");

    private Integer value;
    private String desc;
}

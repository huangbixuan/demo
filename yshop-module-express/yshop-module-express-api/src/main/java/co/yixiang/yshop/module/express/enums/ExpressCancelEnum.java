package co.yixiang.yshop.module.express.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 物品类型对照表枚举
 */
@Getter
@AllArgsConstructor
public enum ExpressCancelEnum {
    TYPE_1(1,"不需要寄件了"),
    TYPE_2(2,"填错订单信息"),
    TYPE_3(3,"配送员要求取消"),
    TYPE_4(4,"暂时无法提供待配送物品"),
    TYPE_5(5,"重复下单，取消此单"),
    TYPE_6(6,"配送员没来取货"),
    TYPE_7(7,"没有配送员接单"),
    TYPE_8(8,"其他");

    private Integer value;
    private String desc;
}

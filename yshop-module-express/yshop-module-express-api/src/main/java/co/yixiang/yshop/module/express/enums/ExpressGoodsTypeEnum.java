package co.yixiang.yshop.module.express.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 物品类型对照表枚举
 */
@Getter
@AllArgsConstructor
public enum ExpressGoodsTypeEnum {
    TYPE_0(0,"文件"),
    TYPE_1(1,"食品"),
    TYPE_2(2,"药品"),
    TYPE_3(3,"蛋糕"),
    TYPE_4(4,"生鲜"),
    TYPE_5(5,"鲜花"),
    TYPE_6(6,"数码"),
    TYPE_7(7,"服饰"),
    TYPE_8(8,"汽配");

    private Integer value;
    private String desc;
}

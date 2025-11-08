package co.yixiang.yshop.module.store.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 类型枚举
 */
@Getter
@AllArgsConstructor
public enum BankTypeEnum {
    TYPE_1(1,"银行卡"),
    TYPE_2(2,"微信"),
    TYPE_3(3,"支付宝");

    private Integer value;
    private String desc;
}

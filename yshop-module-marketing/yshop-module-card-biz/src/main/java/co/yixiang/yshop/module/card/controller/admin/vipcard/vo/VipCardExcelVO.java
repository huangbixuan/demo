package co.yixiang.yshop.module.card.controller.admin.vipcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 会员卡 Excel VO
 *
 * @author yshop
 */
@Data
public class VipCardExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("会员卡名称")
    private String name;

    @ExcelProperty("会员卡样式")
    private String styleImg;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("是否有折扣0-无1-有")
    private Integer isDiscount;

    @ExcelProperty("折扣比例")
    private Integer discount;

    @ExcelProperty("赠送方式 no -无 integral-赠送积分 coupon-优惠券 mony-余额")
    private String igiveMethod;

    @ExcelProperty("赠送积分数量")
    private Integer integral;

    @ExcelProperty("赠送的优惠券")
    private String coupon;

    @ExcelProperty("赠送的余额")
    private BigDecimal mony;

    @ExcelProperty("有效期 单位月0-表示永久")
    private Integer period;

    @ExcelProperty("购买的价格")
    private BigDecimal price;

    @ExcelProperty("0-正常 1-关闭")
    private Integer status;

    @ExcelProperty("使用的规则")
    private String rule;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}

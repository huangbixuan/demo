package co.yixiang.yshop.module.card.controller.app.vipcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* 会员卡  VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppVipCardVO {

    @Schema(description = "id", required = true, example = "30884")
    private Long id;

    @Schema(description = "会员卡名称", required = true, example = "李四")
    @NotNull(message = "会员卡名称不能为空")
    private String name;

    @Schema(description = "会员卡样式", required = true)
    @NotNull(message = "会员卡样式不能为空")
    private String styleImg;

    @Schema(description = "是否有折扣0-无1-有", example = "31583")
    private Integer isDiscount;

    @Schema(description = "折扣比例", example = "16926")
    private Integer discount;

    @Schema(description = "赠送方式 no -无 integral-赠送积分 coupon-优惠券 mony-余额")
    private String giveMethod;

    @Schema(description = "赠送积分数量")
    private Integer integral;


    @Schema(description = "赠送的余额")
    private BigDecimal mony;

    @Schema(description = "有效期 单位月0-表示永久")
    private Integer period;

    @Schema(description = "购买的价格", example = "25392")
    private BigDecimal price;

    @Schema(description = "0-正常 1-关闭", example = "1")
    private Integer status;

    @Schema(description = "使用的规则", required = true)
    @NotNull(message = "使用的规则不能为空")
    private String rule;

}

package co.yixiang.yshop.module.shop.controller.app.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* 充值金额 VO
*/
@Data
public class AppRechargeListVO {

    @Schema(description = "id", required = true, example = "27504")
    private Long id;

    @Schema(description = "标题", required = true, example = "yshop")
    private String name;

    @Schema(description = "销量", required = true)
    private Integer sales;

    @Schema(description = "价值", required = true)
    private BigDecimal value;

    @Schema(description = "权重", required = true)
    private Integer weigh;

    @Schema(description = "销售价", required = true, example = "13493")
    private BigDecimal sellPrice;

}

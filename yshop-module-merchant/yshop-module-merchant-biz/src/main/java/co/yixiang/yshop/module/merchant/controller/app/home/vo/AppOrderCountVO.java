package co.yixiang.yshop.module.merchant.controller.app.home.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AppOrderCountVO {

    @Schema(description = "待出单数量")
    private Long num01;

    @Schema(description = "已出单数量")
    private Long num02;

    @Schema(description = "已完成数量")
    private Long num03;

    @Schema(description = "待退款数量")
    private Long num04;

    @Schema(description = "待支付数量")
    private Long num05;

    @Schema(description = "已退款数量")
    private Long num06;

    @Schema(description = "预约订单")
    private Long num07;

}

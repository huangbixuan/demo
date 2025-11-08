package co.yixiang.yshop.module.merchant.controller.app.home.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class AppTodayDataVO {

    @Schema(description = "今日营业额")
    private BigDecimal num01;

    @Schema(description = "今日订单")
    private Long num02;

    @Schema(description = "今日访客")
    private Integer num03;

    @Schema(description = "今日退单")
    private Long num04;


}

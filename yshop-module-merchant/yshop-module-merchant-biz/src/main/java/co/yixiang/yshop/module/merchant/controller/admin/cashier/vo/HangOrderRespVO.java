package co.yixiang.yshop.module.merchant.controller.admin.cashier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 挂单 Response VO")
@Data
public class HangOrderRespVO {

    @Schema(description = "挂单号")
    private String hangNo;

    @Schema(description = "总价格")
    private BigDecimal totalPrice;


    @Schema(description = "挂单时间")
    private LocalDateTime updateTime;

    private List<StoreCartRespVO> storeCartRespVOS;


}
package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 充值金额管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RechargeRespVO extends RechargeBaseVO {

    @Schema(description = "id", required = true, example = "27504")
    private Long id;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

}

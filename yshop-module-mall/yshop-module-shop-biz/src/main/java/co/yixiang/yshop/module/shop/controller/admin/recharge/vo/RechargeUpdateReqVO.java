package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 充值金额管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RechargeUpdateReqVO extends RechargeBaseVO {

    @Schema(description = "id", required = true, example = "27504")
    @NotNull(message = "id不能为空")
    private Long id;

}

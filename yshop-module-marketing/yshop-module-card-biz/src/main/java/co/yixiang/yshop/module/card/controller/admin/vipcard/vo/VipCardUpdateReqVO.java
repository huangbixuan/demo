package co.yixiang.yshop.module.card.controller.admin.vipcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 会员卡更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VipCardUpdateReqVO extends VipCardBaseVO {

    @Schema(description = "id", required = true, example = "30884")
    @NotNull(message = "id不能为空")
    private Long id;

}

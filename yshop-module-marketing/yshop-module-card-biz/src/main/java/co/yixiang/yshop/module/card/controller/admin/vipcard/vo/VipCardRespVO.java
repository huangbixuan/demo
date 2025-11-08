package co.yixiang.yshop.module.card.controller.admin.vipcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员卡 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VipCardRespVO extends VipCardBaseVO {

    @Schema(description = "id", required = true, example = "30884")
    private Long id;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

}

package co.yixiang.yshop.module.store.controller.admin.userbank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 提现账户 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserBankRespVO extends UserBankBaseVO {

    @Schema(description = "ID", required = true, example = "24615")
    private Long id;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

}

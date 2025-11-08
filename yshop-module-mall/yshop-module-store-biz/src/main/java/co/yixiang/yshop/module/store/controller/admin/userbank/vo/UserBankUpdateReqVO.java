package co.yixiang.yshop.module.store.controller.admin.userbank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 提现账户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserBankUpdateReqVO extends UserBankBaseVO {

    @Schema(description = "ID", required = true, example = "24615")
    @NotNull(message = "ID不能为空")
    private Long id;

}

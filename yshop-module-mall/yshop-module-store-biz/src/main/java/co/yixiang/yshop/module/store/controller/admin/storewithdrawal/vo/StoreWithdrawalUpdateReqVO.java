package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 提现管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWithdrawalUpdateReqVO extends StoreWithdrawalBaseVO {

    @Schema(description = "ID", required = true, example = "3325")
    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(description = "是否是审核", required = true, example = "true")
    private Boolean isCheck;

}

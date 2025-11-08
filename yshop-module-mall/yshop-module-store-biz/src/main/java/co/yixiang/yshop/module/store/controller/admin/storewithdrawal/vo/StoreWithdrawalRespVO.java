package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import co.yixiang.yshop.module.store.controller.admin.userbank.vo.UserBankRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 提现管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWithdrawalRespVO extends StoreWithdrawalBaseVO {

    @Schema(description = "ID", required = true, example = "3325")
    private Long id;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    @Schema(description = "银行卡信息")
    private UserBankRespVO userBank;

}

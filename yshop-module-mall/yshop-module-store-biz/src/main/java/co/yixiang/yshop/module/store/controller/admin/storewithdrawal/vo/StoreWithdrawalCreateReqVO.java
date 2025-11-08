package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 提现管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWithdrawalCreateReqVO extends StoreWithdrawalBaseVO {

}

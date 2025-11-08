package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 充值金额管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RechargeCreateReqVO extends RechargeBaseVO {

}

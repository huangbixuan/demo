package co.yixiang.yshop.module.card.controller.admin.vipcard.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 会员卡创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VipCardCreateReqVO extends VipCardBaseVO {

}

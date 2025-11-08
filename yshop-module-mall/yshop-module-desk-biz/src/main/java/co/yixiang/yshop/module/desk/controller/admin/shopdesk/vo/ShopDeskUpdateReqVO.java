package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 门店 - 桌号更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDeskUpdateReqVO extends ShopDeskBaseVO {

    @Schema(description = "id", required = true, example = "2944")
    @NotNull(message = "id不能为空")
    private Long id;

}

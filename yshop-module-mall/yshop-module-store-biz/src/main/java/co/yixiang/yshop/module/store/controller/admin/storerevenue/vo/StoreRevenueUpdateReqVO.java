package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 店铺收支明细更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreRevenueUpdateReqVO extends StoreRevenueBaseVO {

    @Schema(description = "ID", required = true, example = "22552")
    @NotNull(message = "ID不能为空")
    private Long id;

}

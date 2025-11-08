package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 店铺收支明细 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreRevenueRespVO extends StoreRevenueBaseVO {

    @Schema(description = "ID", required = true, example = "22552")
    private Long id;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    @Schema(description = "用户昵称")
    private String nickname;

}

package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 门店 - 桌号 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDeskRespVO extends ShopDeskBaseVO {

    @Schema(description = "id", required = true, example = "2944")
    private Long id;

    @Schema(description = "添加时间", required = true)
    private LocalDateTime createTime;

}

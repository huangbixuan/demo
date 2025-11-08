package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 易联云打印机 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WebPrintRespVO extends WebPrintBaseVO {

    @Schema(description = "id", required = true, example = "8134")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}

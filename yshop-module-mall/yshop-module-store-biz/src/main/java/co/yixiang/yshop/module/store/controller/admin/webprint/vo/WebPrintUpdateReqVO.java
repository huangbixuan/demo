package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 易联云打印机更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WebPrintUpdateReqVO extends WebPrintBaseVO {

    @Schema(description = "id", required = true, example = "8134")
    @NotNull(message = "id不能为空")
    private Long id;

}

package co.yixiang.yshop.module.score.controller.admin.scoreads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 积分商城广告图管理新增/修改 Request VO")
@Data
public class ScoreAdsSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31942")
    private Long id;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "是否显现", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否显现不能为空")
    private Boolean isSwitch;

    @Schema(description = "权重", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "权重不能为空")
    private Integer weigh;

    @Schema(description = "店铺名称逗号隔开", example = "王五")
    private String shopName;

}
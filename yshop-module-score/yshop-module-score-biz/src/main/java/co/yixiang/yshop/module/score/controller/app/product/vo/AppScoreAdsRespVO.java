package co.yixiang.yshop.module.score.controller.app.product.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "app - 积分商城广告 Response VO")
@Data
public class AppScoreAdsRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31942")
    private Long id;

    @Schema(description = "图片")
    private String image;


}
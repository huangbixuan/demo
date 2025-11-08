package co.yixiang.yshop.module.score.controller.app.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "app - 积分商品分类 Response VO")
@Data
public class AppScoreProductCategoryRespVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11727")
    private Long id;


    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String name;



}
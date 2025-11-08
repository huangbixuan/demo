package co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 门店桌号分类新增/修改 Request VO")
@Data
public class ShopDeskCategorySaveReqVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14558")
    private Long id;

    @Schema(description = "店铺id用'", example = "32516")
    private Long shopId;

    @Schema(description = "父分类编号", example = "29955")
    private Long parentId;

    @Schema(description = "店铺名称", example = "赵六")
    private String shopName;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类图片", example = "https://www.yixiang.co")
    private String picUrl;

    @Schema(description = "人数")
    private Integer people;

    @Schema(description = "分类排序")
    private Integer sort;

    @Schema(description = "分类描述", example = "你说的对")
    private String description;

    @Schema(description = "开启状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "开启状态不能为空")
    private Integer status;

}
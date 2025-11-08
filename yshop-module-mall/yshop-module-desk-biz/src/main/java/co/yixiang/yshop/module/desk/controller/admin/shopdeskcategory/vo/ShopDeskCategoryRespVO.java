package co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 门店桌号分类 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShopDeskCategoryRespVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14558")
    @ExcelProperty("分类编号")
    private Long id;

    @Schema(description = "店铺id用'", example = "32516")
    @ExcelProperty("店铺id用'")
    private Integer shopId;

    @Schema(description = "父分类编号", example = "29955")
    @ExcelProperty("父分类编号")
    private Long parentId;

    @Schema(description = "店铺名称", example = "赵六")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("分类名称")
    private String name;

    @Schema(description = "分类图片", example = "https://www.yixiang.co")
    @ExcelProperty("分类图片")
    private String picUrl;

    @Schema(description = "人数")
    @ExcelProperty("人数")
    private Integer people;

    @Schema(description = "分类排序")
    @ExcelProperty("分类排序")
    private Integer sort;

    @Schema(description = "分类描述", example = "你说的对")
    @ExcelProperty("分类描述")
    private String description;

    @Schema(description = "开启状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("开启状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
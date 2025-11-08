package co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店桌号分类分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDeskCategoryPageReqVO extends PageParam {

    @Schema(description = "店铺id用'", example = "32516")
    private Integer shopId;

    @Schema(description = "父分类编号", example = "29955")
    private Long parentId;

    @Schema(description = "店铺名称", example = "赵六")
    private String shopName;

    @Schema(description = "分类名称", example = "李四")
    private String name;

    @Schema(description = "分类图片", example = "https://www.yixiang.co")
    private String picUrl;

    @Schema(description = "人数")
    private Integer people;

    @Schema(description = "分类排序")
    private Integer sort;

    @Schema(description = "分类描述", example = "你说的对")
    private String description;

    @Schema(description = "开启状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
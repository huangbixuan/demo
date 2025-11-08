package co.yixiang.yshop.module.store.controller.admin.shopduerule.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "管理后台 - 预约规则 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShopDueRuleRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "675")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "店铺id用'", example = "26361")
    @ExcelProperty("店铺id用'")
    private Integer shopId;

    @Schema(description = "店铺名称", example = "张三")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "标签ID", example = "6304")
    @ExcelProperty("标签ID")
    private Long labelId;

    @Schema(description = "标签名称", example = "标签名称")
    private String labelName;

    @Schema(description = "间隔时间单位小时")
    @ExcelProperty("间隔时间单位小时")
    private BigDecimal interval;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private String endTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
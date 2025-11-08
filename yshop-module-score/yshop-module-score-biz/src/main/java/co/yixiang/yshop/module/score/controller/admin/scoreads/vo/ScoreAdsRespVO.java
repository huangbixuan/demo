package co.yixiang.yshop.module.score.controller.admin.scoreads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 积分商城广告图管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScoreAdsRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31942")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "图片")
    @ExcelProperty("图片")
    private String image;

    @Schema(description = "是否显现", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否显现")
    private Integer isSwitch;

    @Schema(description = "权重", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("权重")
    private Integer weigh;

    @Schema(description = "店铺名称逗号隔开", example = "王五")
    @ExcelProperty("店铺名称逗号隔开")
    private String shopName;

    @Schema(description = "添加时间")
    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}
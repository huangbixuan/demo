package co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 预约标签 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShopDueLabelRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4661")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "标签名称")
    @ExcelProperty("标签名称")
    private String title;

    @Schema(description = "店铺id用'", example = "32045")
    @ExcelProperty("店铺id用'")
    private Integer shopId;

    @Schema(description = "店铺名称", example = "芋艿")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
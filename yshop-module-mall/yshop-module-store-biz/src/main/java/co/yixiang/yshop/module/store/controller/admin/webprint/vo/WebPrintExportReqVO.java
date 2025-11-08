package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 易联云打印机 Excel 导出 Request VO，参数和 WebPrintPageReqVO 是一致的")
@Data
public class WebPrintExportReqVO {

    @Schema(description = "打印机名称")
    private String title;

    @Schema(description = "终端号")
    private String mechineCode;

    @Schema(description = "终端密钥")
    private String msign;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

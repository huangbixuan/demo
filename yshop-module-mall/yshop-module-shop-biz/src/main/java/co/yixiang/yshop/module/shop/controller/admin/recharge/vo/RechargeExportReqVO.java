package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 充值金额管理 Excel 导出 Request VO，参数和 RechargePageReqVO 是一致的")
@Data
public class RechargeExportReqVO {

    @Schema(description = "标题", example = "yshop")
    private String name;

    @Schema(description = "销量")
    private Integer sales;

    @Schema(description = "价值")
    private BigDecimal value;

    @Schema(description = "权重")
    private Integer weigh;

    @Schema(description = "状态:1=显示,0=隐藏", example = "2")
    private Integer status;

    @Schema(description = "销售价", example = "13493")
    private BigDecimal sellPrice;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

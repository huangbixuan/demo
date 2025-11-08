package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 店铺收支明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreRevenuePageReqVO extends PageParam {

    @Schema(description = "门店ID", example = "11810")
    private Long shopId;

    @Schema(description = "店铺名称", example = "赵六")
    private String shopName;

    @Schema(description = "类型:1=收入,2=支出", example = "2")
    private Integer type;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "用户", example = "27268")
    private Long uid;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

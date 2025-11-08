package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 提现管理 Excel 导出 Request VO，参数和 StoreWithdrawalPageReqVO 是一致的")
@Data
public class StoreWithdrawalExportReqVO {

    @Schema(description = "用户ID", example = "9568")
    private Long uid;

    @Schema(description = "门店ID", example = "23731")
    private Long shopId;

    @Schema(description = "门店ID", example = "张三")
    private String shopName;

    @Schema(description = "提现金额")
    private BigDecimal amount;

    @Schema(description = "提现方式", example = "2")
    private Byte type;

    @Schema(description = "状态:0=未审核,1=待到账,2=审核拒绝,3=已到账", example = "2")
    private Byte status;

    @Schema(description = "审核拒绝原因")
    private String refuse;

    @Schema(description = "年月")
    private String month;

    @Schema(description = "剩余可提现金额")
    private BigDecimal residueAmount;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

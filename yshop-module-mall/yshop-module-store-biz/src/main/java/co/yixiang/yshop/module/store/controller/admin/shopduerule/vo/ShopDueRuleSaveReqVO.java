package co.yixiang.yshop.module.store.controller.admin.shopduerule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalTime;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 预约规则新增/修改 Request VO")
@Data
public class ShopDueRuleSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "675")
    private Long id;

    @Schema(description = "店铺id用'", example = "26361")
    private Long shopId;

    @Schema(description = "店铺名称", example = "张三")
    private String shopName;

    @Schema(description = "标签ID", example = "6304")
    private Long labelId;

    @Schema(description = "间隔时间单位小时")
    private BigDecimal interval;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

}
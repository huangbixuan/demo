package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
* 店铺收支明细 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class StoreRevenueBaseVO {

    @Schema(description = "门店ID", example = "11810")
    private Long shopId;

    @Schema(description = "店铺名称", required = true, example = "赵六")
    @NotNull(message = "店铺名称不能为空")
    private String shopName;

    @Schema(description = "类型:1=收入,2=支出", example = "2")
    private Integer type;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "用户", example = "27268")
    private Long uid;

    @Schema(description = "是否结算", example = "0")
    private Integer isFinish;

    @Schema(description = "单类型 order-普通订单 withdrawal提现订单")
    private String orderType;


}

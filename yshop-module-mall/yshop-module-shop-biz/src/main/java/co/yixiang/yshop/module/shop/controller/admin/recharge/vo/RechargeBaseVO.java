package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
* 充值金额管理 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class RechargeBaseVO {

    @Schema(description = "标题", required = true, example = "yshop")
    @NotNull(message = "标题不能为空")
    private String name;

    @Schema(description = "销量", required = true)
    private Integer sales;

    @Schema(description = "价值", required = true)
    @NotNull(message = "价值不能为空")
    private BigDecimal value;

    @Schema(description = "权重", required = true)
    private Integer weigh;

    @Schema(description = "状态:1=显示,0=隐藏", required = true, example = "2")
    private Integer status;

    @Schema(description = "销售价", required = true, example = "13493")
    @NotNull(message = "销售价不能为空")
    private BigDecimal sellPrice;

}

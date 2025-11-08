package co.yixiang.yshop.module.merchant.controller.admin.cashier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 购物车新增/修改 Request VO")
@Data
public class StoreCartSaveReqVO {

    @Schema(description = "购物车表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11256")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10667")
    @NotNull(message = "用户ID不能为空")
    private Long uid;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String type;

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1696")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Schema(description = "店铺ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1696")
    private Long shopId;

    @Schema(description = "商品属性", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productAttrUnique;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品数量不能为空")
    private Integer cartNum;

    @Schema(description = "0 = 未购买 1 = 已购买", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPay;

    @Schema(description = "是否为立即购买", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isNew;

    @Schema(description = "拼团id", example = "13847")
    private Integer combinationId;

    @Schema(description = "秒杀产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8790")
    private Integer seckillId;

    @Schema(description = "砍价id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21581")
    private Integer bargainId;

    @Schema(description = "是否挂单0-不是 1-是")
    private Integer isHang;

}
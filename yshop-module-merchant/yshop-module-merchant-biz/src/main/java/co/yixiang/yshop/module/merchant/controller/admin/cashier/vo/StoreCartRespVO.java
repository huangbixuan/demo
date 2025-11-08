package co.yixiang.yshop.module.merchant.controller.admin.cashier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 购物车 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StoreCartRespVO {

    @Schema(description = "购物车表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11256")
    @ExcelProperty("购物车表ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10667")
    @ExcelProperty("用户ID")
    private Long uid;


    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1696")
    @ExcelProperty("商品ID")
    private Long productId;

    @Schema(description = "商品属性", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("商品属性")
    private String sku;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("商品数量")
    private Integer cartNum;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeName;

    @Schema(description = "商品图片", requiredMode = Schema.RequiredMode.REQUIRED)
    private String image;



}
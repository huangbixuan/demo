package co.yixiang.yshop.module.order.controller.app.order.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 桌面订单商品明细表
 * </p>
 *
 * @author hupeng
 * @date 2024-01-10
 */
@Data
@Schema(description = "用户 APP - 桌面订单商品明细表")
public class AppDeskOrderGoodsVo  {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "商品标题")
    private String title;

    @Schema(description = "商品图片")
    private String image;

    @Schema(description = "数量")
    private Integer number;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "规格")
    private String spec;





}

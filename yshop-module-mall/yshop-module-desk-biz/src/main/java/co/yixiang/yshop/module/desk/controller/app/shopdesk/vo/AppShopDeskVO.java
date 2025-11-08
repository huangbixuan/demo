package co.yixiang.yshop.module.desk.controller.app.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* 门店 - 桌号 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppShopDeskVO {

    private Long id;

    @Schema(description = "门店ID", required = true, example = "6456")
    private Long shopId;

    @Schema(description = "店铺名称", required = true, example = "李四")
    private String shopName;

    @Schema(description = "分类ID", required = true, example = "6456")
    private Long cateId;

    @Schema(description = "桌号名称")
    private String title;

    @Schema(description = "编号")
    private String number;

    @Schema(description = "桌面图")
    private String image;

    @Schema(description = "时间标签")
    private List<DayTime> dayTime;

    @Schema(description = "订单号")
    private String lastOrderNo;

    @Schema(description = "订单时间")
    private LocalDateTime lastOrderTime;


}

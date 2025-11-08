package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* 门店 - 桌号 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ShopDeskBaseVO {

    @Schema(description = "门店ID", required = true, example = "6456")
    @NotNull(message = "门店ID不能为空")
    private Long shopId;

    @Schema(description = "店铺名称", required = true, example = "李四")
    //@NotNull(message = "店铺名称不能为空")
    private String shopName;

    @Schema(description = "分类ID", required = true, example = "6456")
    private Long cateId;

    @Schema(description = "桌号分类")
    private String cateName;

    @Schema(description = "桌号名称")
    private String title;

    @Schema(description = "桌面图")
    private String image;

    @Schema(description = "编号")
    private String number;

    @Schema(description = "小程序二维码")
    private String miniQrcode;

    @Schema(description = "H5二维码")
    private String h5Qrcode;

    @Schema(description = "支付宝二维码")
    private String aliQrcode;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "下单数", required = true, example = "6016")
    //@NotNull(message = "下单数不能为空")
    private Integer orderCount;

    @Schema(description = "消费金额", required = true)
    //@NotNull(message = "消费金额不能为空")
    private BigDecimal costAmount;

    @Schema(description = "上次下单编号")
    private String lastOrderNo;

    @Schema(description = "上次下单时间")
    //@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime lastOrderTime;

    @Schema(description = "上次下单状态", example = "1")
    private Integer lastOrderStatus;

    @Schema(description = "状态：1=启用，0=禁用", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 预约状态：1=预约，0=未预约
     */
    private Integer bookStatus;

    /**
     * 预约时间
     */
    private LocalDateTime bookTime;

}

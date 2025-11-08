package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店 - 桌号分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDeskPageReqVO extends PageParam {

    @Schema(description = "门店ID", example = "6456")
    private Long shopId;

    @Schema(description = "店铺名称", example = "李四")
    private String shopName;

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

    @Schema(description = "下单数", example = "6016")
    private Integer orderCount;

    @Schema(description = "消费金额")
    private BigDecimal costAmount;

    @Schema(description = "上次下单编号")
    private String lastOrderNo;

    @Schema(description = "上次下单时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastOrderTime;

    @Schema(description = "上次下单状态", example = "1")
    private Integer lastOrderStatus;

    @Schema(description = "状态：1=启用，2=禁用", example = "1")
    private Integer status;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "工作台订单还是普通订单")
    private String type;

    @Schema(description = "桌面状态 all, empty ,ing")
    private String deskStatus;

    private Long cateId;



}

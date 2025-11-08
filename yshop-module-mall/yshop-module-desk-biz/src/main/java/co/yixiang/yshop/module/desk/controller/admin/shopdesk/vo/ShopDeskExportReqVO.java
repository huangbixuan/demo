package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店 - 桌号 Excel 导出 Request VO，参数和 ShopDeskPageReqVO 是一致的")
@Data
public class ShopDeskExportReqVO {

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
    private Short orderCount;

    @Schema(description = "消费金额")
    private Short costAmount;

    @Schema(description = "上次下单编号")
    private String lastOrderNo;

    @Schema(description = "上次下单时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastOrderTime;

    @Schema(description = "上次下单状态", example = "1")
    private Byte lastOrderStatus;

    @Schema(description = "状态：1=启用，2=禁用", example = "1")
    private Byte status;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 门店 - 桌号 Excel VO
 *
 * @author yshop
 */
@Data
public class ShopDeskExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("门店ID")
    private Long shopId;

    @ExcelProperty("店铺名称")
    private String shopName;

    @ExcelProperty("编号")
    private String number;

    @ExcelProperty("小程序二维码")
    private String miniQrcode;

    @ExcelProperty("H5二维码")
    private String h5Qrcode;

    @ExcelProperty("支付宝二维码")
    private String aliQrcode;

    @ExcelProperty("备注")
    private String note;

    @ExcelProperty("下单数")
    private Short orderCount;

    @ExcelProperty("消费金额")
    private Short costAmount;

    @ExcelProperty("上次下单编号")
    private String lastOrderNo;

    @ExcelProperty("上次下单时间")
    private LocalDateTime lastOrderTime;

    @ExcelProperty("上次下单状态")
    private Byte lastOrderStatus;

    @ExcelProperty("状态：1=启用，2=禁用")
    private Byte status;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}

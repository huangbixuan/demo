package co.yixiang.yshop.module.express.controller.app.express.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 同城快递公司vO
 *
 * @author yshop
 */
@Data
public class SameCityExpressVO {

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "配送距离")
    private String deliveryDistance;

    @Schema(description = "同城寄件订单号")
    private String orderId;

    @Schema(description = "快递单号")
    private String kuaidinum;

    @Schema(description = "预计配送时间")
    private String estimateDeliveryTime;

    @Schema(description = "配送费（折扣价）")
    private String discountFee;

    @Schema(description = "快递公司名称")
    private String expressName;

    @Schema(description = "取消费用")
    private String cancelFee;



}

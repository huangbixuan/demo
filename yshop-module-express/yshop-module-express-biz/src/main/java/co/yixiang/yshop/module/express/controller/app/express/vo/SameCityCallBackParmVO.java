package co.yixiang.yshop.module.express.controller.app.express.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 同城快递公司SameCityCallBackParmVO
 *
 * @author yshop
 */
@Data
public class SameCityCallBackParmVO {

    @Schema(description = "订单ID")
    private String orderId;

    @Schema(description = "快递公司编码")
    private String kuaidicom;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "订单状态描述")
    private String statusDesc;

    @Schema(description = "骑手姓名")
    private String courierName;

    @Schema(description = "骑手电话")
    private String courierMobile;

    @Schema(description = "预计送达时间")
    private String expectFinishTime;



}

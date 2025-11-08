package co.yixiang.yshop.module.express.controller.app.express.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 同城快递公司SameCityCallBackBodyVO
 *
 * @author yshop
 */
@Data
public class SameCityCallBackBodyVO {

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "加密字符串签名")
    private String sign;

    @Schema(description = "参数主体")
    private String param;







}

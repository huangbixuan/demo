package co.yixiang.yshop.module.express.controller.app.express.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 同城快递公司vO
 *
 * @author yshop
 */
@Data
public class SameCityBodyVO {

    @Schema(description = "code")
    private String code;

    @Schema(description = "message")
    private String message;

    @Schema(description = "success")
    private Boolean success;

    @Schema(description = "快递单号")
    private SameCityExpressVO data;





}

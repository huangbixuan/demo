package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 门店 - 桌号二维码 Response VO")
@Data
@Builder
public class DeskQrcodeRespVO {

    @Schema(description = "小程序二维码", required = true, example = "2944")
    private String miniQrcode;

    @Schema(description = "普通二维码", required = true)
    private String normalQrcode;

}

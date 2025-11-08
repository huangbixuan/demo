package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 桌号 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QrcodeVO {

    @Schema(description = "桌面ID", required = true)
    private Long id;

    @Schema(description = "桌面码", required = true)
    private String number;

    @Schema(description = "门店ID", required = true)
    private String shopId;

    @Schema(description = "小程序环境", required = true)
    private String env;



}

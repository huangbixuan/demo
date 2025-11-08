package co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 预约标签新增/修改 Request VO")
@Data
public class ShopDueLabelSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4661")
    private Long id;

    @Schema(description = "标签名称")
    private String title;

    @Schema(description = "店铺id用'", example = "32045")
    private Long shopId;

    @Schema(description = "店铺名称", example = "芋艿")
    private String shopName;

}
package co.yixiang.yshop.module.member.controller.admin.storeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 门店移动端商家用户关联新增/修改 Request VO")
@Data
public class StoreUserSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10579")
    private Long id;

    @Schema(description = "门店ID", example = "7809")
    private Long shopId;

    @Schema(description = "用户id", example = "19316")
    private Long uid;

    @Schema(description = "0-禁止 1-开启", example = "2")
    private Integer status;

}
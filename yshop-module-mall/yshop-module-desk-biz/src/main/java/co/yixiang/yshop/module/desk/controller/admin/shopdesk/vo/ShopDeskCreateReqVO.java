package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 门店 - 桌号创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDeskCreateReqVO extends ShopDeskBaseVO {

}

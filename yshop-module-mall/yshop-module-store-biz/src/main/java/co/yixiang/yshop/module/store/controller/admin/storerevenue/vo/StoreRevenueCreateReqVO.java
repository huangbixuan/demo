package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 店铺收支明细创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreRevenueCreateReqVO extends StoreRevenueBaseVO {

}

package co.yixiang.yshop.module.merchant.controller.app.home.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 门店管理VO
*/
@Data
public class ShopReqVO {

    private Long id;

    @Schema(description = "店铺名称", required = true, example = "李四")
    @NotEmpty(message = "店铺名称不能为空")
    private String name;

    @Schema(description = "店铺电话", required = true)
    @NotEmpty(message = "店铺电话不能为空")
    private String mobile;

    @Schema(description = "详细地址", required = true)
    @NotEmpty(message = "详细地址不能为空")
    private String address;

    @Schema(description = "地图定位地址", required = true)
    @NotEmpty(message = "地图定位地址不能为空")
    private String addressMap;

    @Schema(description = "经度", required = true)
    private String lng;

    @Schema(description = "纬度", required = true)
    private String lat;

    @Schema(description = "营业开始时间", required = true)
    @NotNull(message = "营业开始时间不能为空")
    private Date startTime;

    @Schema(description = "营业结束时间", required = true)
    @NotNull(message = "营业结束时间不能为空")
    private Date endTime;



}

package co.yixiang.yshop.module.store.controller.admin.storerevenue.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 店铺收支明细 Excel VO
 *
 * @author yshop
 */
@Data
public class StoreRevenueExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("门店ID")
    private Long shopId;

    @ExcelProperty("店铺名称")
    private String shopName;

    @ExcelProperty("类型:1=收入,2=支出")
    private Integer type;

    @ExcelProperty("金额")
    private BigDecimal amount;

    @ExcelProperty("用户")
    private Long uid;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}

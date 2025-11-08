package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 提现管理 Excel VO
 *
 * @author yshop
 */
@Data
public class StoreWithdrawalExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户ID")
    private Long uid;

    @ExcelProperty("门店ID")
    private Long shopId;

    @ExcelProperty("门店ID")
    private String shopName;

    @ExcelProperty("提现金额")
    private BigDecimal amount;

    @ExcelProperty("提现方式")
    private Integer type;

    @ExcelProperty("状态:0=未审核,1=待到账,2=审核拒绝,3=已到账")
    private Integer status;

    @ExcelProperty("审核拒绝原因")
    private String refuse;

    @ExcelProperty("年月")
    private String month;

    @ExcelProperty("剩余可提现金额")
    private BigDecimal residueAmount;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}

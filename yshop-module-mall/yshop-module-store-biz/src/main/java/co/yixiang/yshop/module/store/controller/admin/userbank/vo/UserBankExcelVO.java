package co.yixiang.yshop.module.store.controller.admin.userbank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 提现账户 Excel VO
 *
 * @author yshop
 */
@Data
public class UserBankExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("店铺名称")
    private String shopName;

    @ExcelProperty("用户ID")
    private String uid;

    @ExcelProperty("提现类型 1=银行卡 2=微信 3=支付宝")
    private Integer type;

    @ExcelProperty("银行名称")
    private String bankName;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("银行卡号")
    private String bankCode;

    @ExcelProperty("手机号")
    private String bankMobile;

    @ExcelProperty("支付宝账号")
    private String zfbCode;

    @ExcelProperty("微信账号")
    private String wxCode;

    @ExcelProperty("支付宝收款码")
    private String zfbImg;

    @ExcelProperty("微信收款码")
    private String wxImg;

    @ExcelProperty("状态:0=审核中,1=审核通过,2=审核拒绝")
    private Integer status;

    @ExcelProperty("审核拒绝原因")
    private String refuse;

    @ExcelProperty("图标")
    private String bankIcon;

    @ExcelProperty("银行卡类型")
    private String bankType;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}

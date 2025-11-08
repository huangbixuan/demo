package co.yixiang.yshop.module.store.dal.dataobject.userbank;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 提现账户 DO
 *
 * @author yshop
 */
@TableName("yshop_user_bank")
@KeySequence("yshop_user_bank_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBankDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 提现类型 1=银行卡 2=微信 3=支付宝
     */
    private Integer type;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 银行卡号
     */
    private String bankCode;
    /**
     * 手机号
     */
    private String bankMobile;
    /**
     * 支付宝账号
     */
    private String zfbCode;
    /**
     * 微信账号
     */
    private String wxCode;
    /**
     * 支付宝收款码
     */
    private String zfbImg;
    /**
     * 微信收款码
     */
    private String wxImg;
    /**
     * 状态:0=审核中,1=审核通过,2=审核拒绝
     */
    private Integer status;
    /**
     * 审核拒绝原因
     */
    private String refuse;
    /**
     * 图标
     */
    private String bankIcon;
    /**
     * 银行卡类型
     */
    private String bankType;

}

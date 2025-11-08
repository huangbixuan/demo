package co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 提现管理 DO
 *
 * @author yshop
 */
@TableName("yshop_store_withdrawal")
@KeySequence("yshop_store_withdrawal_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWithdrawalDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 门店ID
     */
    private Long shopId;
    /**
     * 门店ID
     */
    private String shopName;
    /**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 提现方式
     */
    private Integer type;
    /**
     * 银行卡ID
     */
    private Long bankId;
    /**
     * 状态:0=未审核,1=待到账,2=审核拒绝,3=已到账
     */
    private Integer status;
    /**
     * 审核拒绝原因
     */
    private String refuse;
    /**
     * 年月
     */
    private String month;
    /**
     * 剩余可提现金额
     */
    private BigDecimal residueAmount;

    //提现编号唯一
    private String outBillNo;

   // 备注
    private String remark;

    //微信转账返回的信息
    private String packageInfo;

    //微信转账返回的状态
    private String state;

}

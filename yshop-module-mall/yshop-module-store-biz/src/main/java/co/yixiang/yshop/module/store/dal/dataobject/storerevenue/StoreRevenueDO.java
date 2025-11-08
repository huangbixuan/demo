package co.yixiang.yshop.module.store.dal.dataobject.storerevenue;

import co.yixiang.yshop.framework.tenant.core.db.TenantBaseDO;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 店铺收支明细 DO
 *
 * @author yshop
 */
@TableName("yshop_store_revenue")
@KeySequence("yshop_store_revenue_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRevenueDO extends TenantBaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;

    private String orderId;

    private String orderType;
    /**
     * 门店ID
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 类型:1=收入,2=支出
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 用户
     */
    private Long uid;

    private String nickname;

    /**
     * 是否结算 0-未 1已经
     */
    private Integer isFinish;

}

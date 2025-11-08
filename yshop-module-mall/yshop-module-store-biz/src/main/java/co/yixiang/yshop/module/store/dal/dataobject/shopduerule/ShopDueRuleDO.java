package co.yixiang.yshop.module.store.dal.dataobject.shopduerule;

import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 预约规则 DO
 *
 * @author yshop
 */
@TableName("yshop_shop_due_rule")
@KeySequence("yshop_shop_due_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDueRuleDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 店铺id用'
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 标签ID
     */
    private Long labelId;
    /**
     * 间隔时间单位小时
     */
    @TableField(value = "`interval`")
    private BigDecimal interval;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

}
package co.yixiang.yshop.module.shop.dal.dataobject.recharge;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 充值金额管理 DO
 *
 * @author yshop
 */
@TableName("yshop_recharge")
@KeySequence("yshop_recharge_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 标题
     */
    private String name;
    /**
     * 销量
     */
    private Integer sales;
    /**
     * 价值
     */
    private BigDecimal value;
    /**
     * 权重
     */
    private Integer weigh;
    /**
     * 状态:1=显示,0=隐藏
     */
    private Integer status;
    /**
     * 销售价
     */
    private BigDecimal sellPrice;

}

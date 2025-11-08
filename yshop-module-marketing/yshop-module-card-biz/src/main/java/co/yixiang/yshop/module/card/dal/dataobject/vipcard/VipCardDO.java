package co.yixiang.yshop.module.card.dal.dataobject.vipcard;

import co.yixiang.yshop.framework.tenant.core.db.TenantBaseDO;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员卡 DO
 *
 * @author yshop
 */
@TableName("yshop_vip_card")
@KeySequence("yshop_vip_card_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipCardDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 会员卡名称
     */
    private String name;
    /**
     * 会员卡样式
     */
    private String styleImg;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否有折扣0-无1-有
     */
    private Integer isDiscount;
    /**
     * 折扣比例
     */
    private Integer discount;
    /**
     * 赠送方式 no -无 integral-赠送积分 coupon-优惠券 mony-余额
     */
    private String giveMethod;
    /**
     * 赠送积分数量
     */
    private Integer integral;
    /**
     * 赠送的优惠券
     */
    private String coupon;
    /**
     * 赠送的余额
     */
    private BigDecimal mony;
    /**
     * 有效期 单位月0-表示永久
     */
    private Integer period;
    /**
     * 购买的价格
     */
    private BigDecimal price;
    /**
     * 0-正常 1-关闭
     */
    private Integer status;
    /**
     * 使用的规则
     */
    private String rule;

}

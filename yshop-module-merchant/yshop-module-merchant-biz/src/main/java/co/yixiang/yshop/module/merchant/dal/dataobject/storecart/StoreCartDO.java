package co.yixiang.yshop.module.merchant.dal.dataobject.storecart;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 购物车 DO
 *
 * @author yshop
 */
@TableName("yshop_store_cart")
@KeySequence("yshop_store_cart_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCartDO extends BaseDO {

    /**
     * 购物车表ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;

    private Long shopId;

    /**
     * 类型
     */
    private String type;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品属性
     */
    private String productAttrUnique;
    /**
     * 商品数量
     */
    private Integer cartNum;
    /**
     * 0 = 未购买 1 = 已购买
     */
    private Integer isPay;
    /**
     * 是否为立即购买
     */
    private Integer isNew;
    /**
     * 拼团id
     */
    private Integer combinationId;
    /**
     * 秒杀产品ID
     */
    private Integer seckillId;
    /**
     * 砍价id
     */
    private Integer bargainId;
    /**
     * 是否挂单0-不是 1-是
     */
    private Integer isHang;

    /**
     * 挂单👌
     */
    private String hangNo;

}
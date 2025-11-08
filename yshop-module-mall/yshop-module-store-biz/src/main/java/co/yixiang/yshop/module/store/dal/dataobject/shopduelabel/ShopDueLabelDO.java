package co.yixiang.yshop.module.store.dal.dataobject.shopduelabel;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 预约标签 DO
 *
 * @author yshop
 */
@TableName("yshop_shop_due_label")
@KeySequence("yshop_shop_due_label_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDueLabelDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 标签名称
     */
    private String title;
    /**
     * 店铺id用'
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;

}
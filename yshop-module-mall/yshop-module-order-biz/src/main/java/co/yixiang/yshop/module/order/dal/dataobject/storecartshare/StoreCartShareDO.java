package co.yixiang.yshop.module.order.dal.dataobject.storecartshare;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 购物车共享菜单 DO
 *
 * @author yshop
 */
@TableName(value="yshop_store_cart_share",autoResultMap = true)
@KeySequence("yshop_store_cart_share_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCartShareDO extends BaseDO {

    /**
     * 购物车表ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 用户名
     */
    private String uName;
    /**
     * 桌面ID
     */
    private Long deskId;
    /**
     * json信息
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray content;

}
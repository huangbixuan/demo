package co.yixiang.yshop.module.member.dal.dataobject.storeuser;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门店移动端商家用户关联 DO
 *
 * @author yshop
 */
@TableName("yshop_store_user")
@KeySequence("yshop_store_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreUserDO extends BaseDO {

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
     * 用户id
     */
    private Long uid;
    /**
     * 0-禁止 1-开启
     */
    private Integer status;

}
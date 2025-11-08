package co.yixiang.yshop.module.score.dal.dataobject.scoreads;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 积分商城广告图管理 DO
 *
 * @author yshop
 */
@TableName("yshop_score_ads")
@KeySequence("yshop_score_ads_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreAdsDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 图片
     */
    private String image;
    /**
     * 是否显现
     */
    private Integer isSwitch;
    /**
     * 权重
     */
    private Integer weigh;
    /**
     * 店铺名称逗号隔开
     */
    private String shopName;

}
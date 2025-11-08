package co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 积分商品分类 DO
 *
 * @author yshop
 */
@TableName("yshop_score_product_category")
@KeySequence("yshop_score_product_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreProductCategoryDO extends BaseDO {

    /**
     * 分类编号
     */
    @TableId
    private Long id;
    /**
     * 父分类编号
     */
    private Long parentId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类图片
     */
    private String picUrl;
    /**
     * 分类排序
     */
    private Integer sort;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 开启状态
     */
    private Integer status;

}
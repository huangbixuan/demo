package co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门店桌号分类 DO
 *
 * @author yshop
 */
@TableName("yshop_shop_desk_category")
@KeySequence("yshop_shop_desk_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDeskCategoryDO extends BaseDO {

    /**
     * 分类编号
     */
    @TableId
    private Long id;
    /**
     * 店铺id用'
     */
    private Long shopId;
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
     * 人数
     */
    private Integer people;
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
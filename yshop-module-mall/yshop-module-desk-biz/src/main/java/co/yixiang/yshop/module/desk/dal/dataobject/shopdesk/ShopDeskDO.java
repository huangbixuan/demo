package co.yixiang.yshop.module.desk.dal.dataobject.shopdesk;

import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门店 - 桌号 DO
 *
 * @author yshop
 */
@TableName("yshop_shop_desk")
@KeySequence("yshop_shop_desk_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDeskDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 门店ID
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;


    private Long cateId;

    private String cateName;

    private String title;

    private String image;
    /**
     * 编号
     */
    private String number;
    /**
     * 小程序二维码
     */
    private String miniQrcode;
    /**
     * H5二维码
     */
    private String h5Qrcode;
    /**
     * 支付宝二维码
     */
    private String aliQrcode;
    /**
     * 备注
     */
    private String note;
    /**
     * 下单数
     */
    private Integer orderCount;
    /**
     * 消费金额
     */
    private BigDecimal costAmount;
    /**
     * 上次下单编号
     */
    @TableField(updateStrategy= FieldStrategy.ALWAYS)
    private String lastOrderNo;
    /**
     * 上次下单时间
     */
    @TableField(updateStrategy= FieldStrategy.ALWAYS)
    private LocalDateTime lastOrderTime;
    /**
     * 最后一次下单状态0 -完成 1-进行中
     */
    private Integer lastOrderStatus;
    /**
     * 状态：1=启用，0=禁用
     */
    private Integer status;

    /**
     * 预约状态：1=预约，0=未预约
     */
    private Integer bookStatus;

    /**
     * 预约时间
     */
    @TableField(updateStrategy= FieldStrategy.ALWAYS)
    private LocalDateTime bookTime;

}

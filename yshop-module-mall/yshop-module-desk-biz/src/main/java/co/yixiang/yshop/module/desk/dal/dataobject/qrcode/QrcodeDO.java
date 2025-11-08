package co.yixiang.yshop.module.desk.dal.dataobject.qrcode;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 二维码 DO
 *
 * @author yshop
 */
@TableName("yshop_qrcode")
@KeySequence("yshop_qrcode_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 类别
     */
    private String type;
    /**
     * 二维码内容hash
     */
    private String hash;
    /**
     * 数据
     */
    private String data;
    /**
     * 路径
     */
    private String src;

    /**
     * 全路径
     */
    private String fullPath;

}

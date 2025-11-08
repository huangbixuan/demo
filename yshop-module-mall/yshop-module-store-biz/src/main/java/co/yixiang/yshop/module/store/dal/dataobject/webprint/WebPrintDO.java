package co.yixiang.yshop.module.store.dal.dataobject.webprint;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 易联云打印机 DO
 *
 * @author yshop
 */
@TableName("yshop_web_print")
@KeySequence("yshop_web_print_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebPrintDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 打印机名称
     */
    private String title;

    /**
     * 打印机品牌
     */
    private String brand;

    /**
     * 终端号
     */
    private String mechineCode;
    /**
     * 终端密钥
     */
    private String msign;



}

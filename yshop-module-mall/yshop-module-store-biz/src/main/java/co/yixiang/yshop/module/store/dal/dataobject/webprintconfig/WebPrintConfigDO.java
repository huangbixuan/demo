package co.yixiang.yshop.module.store.dal.dataobject.webprintconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打印机配置 DO
 *
 * @author yshop
 */
@TableName("yshop_web_print_config")
@KeySequence("yshop_web_print_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebPrintConfigDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 打印机品牌
     */
    private String brand;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用密钥
     */
    private String appSecret;
    /**
     * 打印模版
     */
    private String template;

    /**
     * token
     */
    private String accessToken;

}
package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 打印机配置 WebPrintSetVO")
@Data
@ToString(callSuper = true)
public class WebPrintSetVO {

    /**
     * id
     */
    private Long id;
    /**
     * 打印机品牌
     */
    private String brand;
    /**
     * 应用ID
     */
    @NotEmpty(message = "应用ID或者user不能为空")
    private String appId;
    /**
     * 应用密钥
     */
    @NotEmpty(message = "应用密钥或者ukey不能为空")
    private String appSecret;
    /**
     * 打印模版
     */
    @NotEmpty(message = "打印模版不能为空")
    private String template;




}

package co.yixiang.yshop.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户购买会员卡 vo
 *
 * @author yshop
 */
@Data
public class AppUserCardVO {

    @Schema(description = "会员卡ID", required = true)
    private String cardId;




}

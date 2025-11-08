package co.yixiang.yshop.module.member.api.user.dto;

import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 用户信息 Response DTO
 *
 * @author yshop
 */
@Data
public class MemberUserRespDTO {

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 手机
     */
    private String mobile;

    //公众号openid
    private String openid;

    //小程序openid
    private String routineOpenid;

    //用户登陆类型，h5,wechat,routine
    private String loginType;

}

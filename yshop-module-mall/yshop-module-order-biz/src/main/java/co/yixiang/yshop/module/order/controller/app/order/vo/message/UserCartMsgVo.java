/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.order.controller.app.order.vo.message;

import lombok.Data;

import java.util.List;

/**
 * @ClassName UserCartMsgVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2025/02/16
 **/

@Data
public class UserCartMsgVo {

    private Long shopId;

    private Long deskId;

    private Long uid;

    private String uName;

    private List<CartMsgVo> content;


}

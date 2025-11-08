/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.yshop.module.order.controller.app.order.vo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName CartMsgVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2025/02/16
 **/

@Data
public class CartMsgVo {

    private Long id;

    @JsonProperty("cate_id")
    private Long CateId;

    private String name;

    private BigDecimal price;

    private Integer number;

    private String image;

    private String valueStr;


}

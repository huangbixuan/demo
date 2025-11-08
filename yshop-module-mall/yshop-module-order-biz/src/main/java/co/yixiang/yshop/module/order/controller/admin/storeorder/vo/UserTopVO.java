package co.yixiang.yshop.module.order.controller.admin.storeorder.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTopVO {
    private Long id;

    private String nickname;

    private BigDecimal price;
}

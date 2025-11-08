package co.yixiang.yshop.module.order.api.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class AppStoreOrderDTO  {

    private Long id;

    private String orderId;

    private String extendOrderId;

    private Long uid;

    private String realName;

    private String userPhone;

    private String userAddress;

    private String cartId;

    private Integer totalNum;

    private BigDecimal totalPrice;

    private BigDecimal totalPostage;

    private BigDecimal payPrice;

    private BigDecimal payIntegral;

    private BigDecimal payPostage;

    private BigDecimal deductionPrice;

    private Integer couponId;

    private BigDecimal couponPrice;

    private Integer paid;

    private LocalDateTime payTime;

    private Integer status;

    private Integer refundStatus;

    private BigDecimal refundPrice;

    private Integer numberId;


    private String orderType;

    private Long shopId;

    private String shopName;

    private Long deskId;

    private String deskNumber;

    private Integer deskPeople;


}

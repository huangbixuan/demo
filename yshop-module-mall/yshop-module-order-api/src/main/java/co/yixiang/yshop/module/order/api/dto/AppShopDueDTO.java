package co.yixiang.yshop.module.order.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class AppShopDueDTO {

    private Long uid;

    private Long deskId;

    private LocalDateTime dueTime;

    private String reachTime;

    private String realName;

    private String userPhone;

    private Integer deskPeople;


}

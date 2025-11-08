package co.yixiang.yshop.module.order.controller.admin.storeorder.vo;

import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.member.controller.admin.user.vo.UserRespVO;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppDeskOrderVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 订单 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderRespVO extends StoreOrderBaseVO {

    @Schema(description = "订单ID", required = true, example = "31716")
    private Long id;

    @Schema(description = "添加时间", required = true)
    private LocalDateTime createTime;

    @Schema(description = "用户信息", required = true)
    private UserRespVO userRespVO;

    @Schema(description = "商品信息", required = true)
    private List<StoreOrderCartInfoDO> storeOrderCartInfoDOList;

    @Schema(description = "订单状态", required = true)
    private String StatusStr;

    private ShopDeskDO shopDeskDO;

    private Integer dueStatus;

    //预约时间
    private LocalDateTime dueTime;

    //到店时间
    private String reachTime;

    @Schema(description = "桌面订单明细信息")
    private List<AppDeskOrderVo> appDeskOrderVo;


    private String sameCityTaskId;

    private String sameCityOrderId;

    private String sameCityDeliveryDistance;

    private String sameCityDeliveryTime;

    private Integer sameCityDeliveryStatus;

    private String sameCityDeliveryStatusDes;

    private String sameCityDeliveryCourierName;

    private String sameCityDeliveryCourierMobile;

    private String sameCityDeliveryExpectFinishTime;

    private String expressName;



}

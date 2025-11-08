package co.yixiang.yshop.module.order.controller.app.order.vo;


import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDeskVO;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserInfoRespVO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.service.storeorder.dto.StatusDto;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 桌面点餐订单明细
 * </p>
 *
 * @author hupeng
 * @date 2025-01-10
 */
@Data
@Schema(description = "用户 APP - 桌面点餐订单明细")
public class AppDeskOrderVo  {

    @Schema(description = "ID")
    private Long uid;

    @Schema(description = "用户昵称")
    private String uNickname;

    @Schema(description = "用户头像")
    private String uAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Schema(description = "加餐时间")
    private LocalDateTime addTime;

    @Schema(description = "是否出单")
    private Integer isOrder;

    @Schema(description = "用户点餐类型")
    private String uidType;

    private Integer addProductMark;

    private List<AppDeskOrderGoodsVo> appDeskOrderGoodsVos;






}

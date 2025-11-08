package co.yixiang.yshop.module.merchant.controller.app.home.vo;

import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AppHomeVO {

    @Schema(description = "店铺信息")
    private AppStoreShopVO appStoreShopVO;

    @Schema(description = "今日数据统计")
    private AppTodayDataVO appTodayDataVO;

    @Schema(description = "订单数据统计")
    private AppOrderCountVO appOrderCountVO;



}

package co.yixiang.yshop.module.merchant.service.home;


import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppHomeVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.ShopReqVO;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 会员卡 Service 接口
 *
 * @author yshop
 */
public interface AppHomeService   {

    AppHomeVO getData(Long uid);

    void changeStatus(Long shopId, Integer status);

    Long check(Long uid);

    AppStoreShopVO updateShop(ShopReqVO shopReqVO);


}

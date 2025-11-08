package co.yixiang.yshop.module.desk.service.shopdesk;

import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppDeskDueVO;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDeskVO;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDueParam;

import java.util.List;

/**
 * 门店 - 桌号 Service 接口
 *
 * @author yshop
 */
public interface AppShopDeskService {

    /**
     * 获取桌面列表
     * @param shopId 门店ID
     * @param deskStatus 状态
     * @return
     */
    List<AppDeskDueVO> getList(Long shopId,String deskStatus);

    /**
     * 获取桌面预约详情
     * @param deskId 桌面ID
     * @return
     */
    AppShopDeskVO getDetail(Long deskId);


    /**
     * 获取桌面信息
     * @param deskId 桌面ID
     * @return
     */
    AppShopDeskVO getDesk(Long deskId);



    /**
     * 创建预约订单
     * @param uid 用户ID
     * @param param AppShopDueParam
     * @return 返回订单号
     */
    String createDueOrder(Long uid,AppShopDueParam param);





}

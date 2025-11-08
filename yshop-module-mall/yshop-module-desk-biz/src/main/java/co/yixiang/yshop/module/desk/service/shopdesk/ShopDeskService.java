package co.yixiang.yshop.module.desk.service.shopdesk;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 门店 - 桌号 Service 接口
 *
 * @author yshop
 */
public interface ShopDeskService {

    /**
     * 创建门店 - 桌号
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShopDesk(@Valid ShopDeskCreateReqVO createReqVO);

    /**
     * 批量添加
     * @param createReqVO
     */
    void batchAdd(@Valid ShopDeskCreateBatchVO createReqVO);

    /**
     * 更新门店 - 桌号
     *
     * @param updateReqVO 更新信息
     */
    void updateShopDesk(@Valid ShopDeskUpdateReqVO updateReqVO);

    /**
     * 删除门店 - 桌号
     *
     * @param id 编号
     */
    void deleteShopDesk(Long id);

    /**
     * 获得门店 - 桌号
     *
     * @param id 编号
     * @return 门店 - 桌号
     */
    ShopDeskDO getShopDesk(Long id);

    /**
     * 获得门店 - 桌号列表
     *
     * @param ids 编号
     * @return 门店 - 桌号列表
     */
    List<ShopDeskDO> getShopDeskList(Collection<Long> ids);

    /**
     * 获得门店 - 桌号分页
     *
     * @param pageReqVO 分页查询
     * @return 门店 - 桌号分页
     */
    PageResult<ShopDeskDO> getShopDeskPage(ShopDeskPageReqVO pageReqVO);

    /**
     * 获得门店 - 桌号列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 门店 - 桌号列表
     */
    List<ShopDeskDO> getShopDeskList(ShopDeskExportReqVO exportReqVO);

    /**
     * 批量导入桌号
     *
     * @param importDesks     导入桌号列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果
     */
    DeskImportRespVO importShopDeskList(List<ShopDeskTemplateExcelVO> importDesks, boolean isUpdateSupport);


    /**
     * 组合二维码
     * @param id
     * @return
     */
    Map<String, String> mapQrcode(Long id);


}

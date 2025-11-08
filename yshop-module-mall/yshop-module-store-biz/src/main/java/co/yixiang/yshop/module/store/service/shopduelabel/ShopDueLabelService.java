package co.yixiang.yshop.module.store.service.shopduelabel;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 预约标签 Service 接口
 *
 * @author yshop
 */
public interface ShopDueLabelService {

    /**
     * 创建预约标签
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShopDueLabel(@Valid ShopDueLabelSaveReqVO createReqVO);

    /**
     * 更新预约标签
     *
     * @param updateReqVO 更新信息
     */
    void updateShopDueLabel(@Valid ShopDueLabelSaveReqVO updateReqVO);

    /**
     * 删除预约标签
     *
     * @param id 编号
     */
    void deleteShopDueLabel(Long id);

    /**
     * 获得预约标签
     *
     * @param id 编号
     * @return 预约标签
     */
    ShopDueLabelDO getShopDueLabel(Long id);

    /**
     * 获得预约标签分页
     *
     * @param pageReqVO 分页查询
     * @return 预约标签分页
     */
    PageResult<ShopDueLabelDO> getShopDueLabelPage(ShopDueLabelPageReqVO pageReqVO);

}
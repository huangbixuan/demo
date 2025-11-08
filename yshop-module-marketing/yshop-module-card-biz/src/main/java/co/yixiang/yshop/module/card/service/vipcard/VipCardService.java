package co.yixiang.yshop.module.card.service.vipcard;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.card.controller.admin.vipcard.vo.*;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 会员卡 Service 接口
 *
 * @author yshop
 */
public interface VipCardService {

    /**
     * 创建会员卡
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipCard(@Valid VipCardCreateReqVO createReqVO);

    /**
     * 更新会员卡
     *
     * @param updateReqVO 更新信息
     */
    void updateVipCard(@Valid VipCardUpdateReqVO updateReqVO);

    /**
     * 删除会员卡
     *
     * @param id 编号
     */
    void deleteVipCard(Long id);

    /**
     * 获得会员卡
     *
     * @param id 编号
     * @return 会员卡
     */
    VipCardDO getVipCard(Long id);

    /**
     * 获得会员卡列表
     *
     * @param ids 编号
     * @return 会员卡列表
     */
    List<VipCardDO> getVipCardList(Collection<Long> ids);

    /**
     * 获得会员卡分页
     *
     * @param pageReqVO 分页查询
     * @return 会员卡分页
     */
    PageResult<VipCardDO> getVipCardPage(VipCardPageReqVO pageReqVO);

    /**
     * 获得会员卡列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 会员卡列表
     */
    List<VipCardDO> getVipCardList(VipCardExportReqVO exportReqVO);

}

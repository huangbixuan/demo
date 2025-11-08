package co.yixiang.yshop.module.score.service.scoreads;

import java.util.*;

import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreAdsRespVO;
import jakarta.validation.*;
import co.yixiang.yshop.module.score.controller.admin.scoreads.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreads.ScoreAdsDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 积分商城广告图管理 Service 接口
 *
 * @author yshop
 */
public interface ScoreAdsService {

    /**
     * 获取轮播图
     * @return
     */
    List<AppScoreAdsRespVO> getAds();

    /**
     * 创建积分商城广告图管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAds(@Valid ScoreAdsSaveReqVO createReqVO);

    /**
     * 更新积分商城广告图管理
     *
     * @param updateReqVO 更新信息
     */
    void updateAds(@Valid ScoreAdsSaveReqVO updateReqVO);

    /**
     * 删除积分商城广告图管理
     *
     * @param id 编号
     */
    void deleteAds(Long id);

    /**
     * 获得积分商城广告图管理
     *
     * @param id 编号
     * @return 积分商城广告图管理
     */
    ScoreAdsDO getAds(Long id);

    /**
     * 获得积分商城广告图管理分页
     *
     * @param pageReqVO 分页查询
     * @return 积分商城广告图管理分页
     */
    PageResult<ScoreAdsDO> getAdsPage(ScoreAdsPageReqVO pageReqVO);

}
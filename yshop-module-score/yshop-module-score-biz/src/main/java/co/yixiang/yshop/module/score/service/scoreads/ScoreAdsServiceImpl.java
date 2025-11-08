package co.yixiang.yshop.module.score.service.scoreads;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreAdsRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import co.yixiang.yshop.module.score.controller.admin.scoreads.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreads.ScoreAdsDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.score.dal.mysql.scoreads.ScoreAdsMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.score.enums.ErrorCodeConstants.*;

/**
 * 积分商城广告图管理 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ScoreAdsServiceImpl implements ScoreAdsService {

    @Resource
    private ScoreAdsMapper adsMapper;


    @Override
    public List<AppScoreAdsRespVO> getAds() {
        List<ScoreAdsDO> scoreAdsDOS = adsMapper.selectList(new LambdaQueryWrapper<ScoreAdsDO>()
                .eq(ScoreAdsDO::getIsSwitch, ShopCommonEnum.IS_STATUS_1.getValue())
                .orderByDesc(ScoreAdsDO::getWeigh));
        return BeanUtils.toBean(scoreAdsDOS,AppScoreAdsRespVO.class);
    }

    @Override
    public Long createAds(ScoreAdsSaveReqVO createReqVO) {
        // 插入
        ScoreAdsDO ads = BeanUtils.toBean(createReqVO, ScoreAdsDO.class);
        adsMapper.insert(ads);
        // 返回
        return ads.getId();
    }

    @Override
    public void updateAds(ScoreAdsSaveReqVO updateReqVO) {
        // 校验存在
        validateAdsExists(updateReqVO.getId());
        // 更新
        ScoreAdsDO updateObj = BeanUtils.toBean(updateReqVO, ScoreAdsDO.class);
        adsMapper.updateById(updateObj);
    }

    @Override
    public void deleteAds(Long id) {
        // 校验存在
        validateAdsExists(id);
        // 删除
        adsMapper.deleteById(id);
    }

    private void validateAdsExists(Long id) {
        if (adsMapper.selectById(id) == null) {
            throw exception(ADS_NOT_EXISTS);
        }
    }

    @Override
    public ScoreAdsDO getAds(Long id) {
        return adsMapper.selectById(id);
    }

    @Override
    public PageResult<ScoreAdsDO> getAdsPage(ScoreAdsPageReqVO pageReqVO) {
        return adsMapper.selectPage(pageReqVO);
    }

}
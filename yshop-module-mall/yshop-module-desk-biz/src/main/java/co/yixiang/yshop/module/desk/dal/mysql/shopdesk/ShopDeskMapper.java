package co.yixiang.yshop.module.desk.dal.mysql.shopdesk;

import java.time.LocalDateTime;
import java.util.*;

import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.OrderTypeEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.enums.DeskStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;

/**
 * 门店 - 桌号 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ShopDeskMapper extends BaseMapperX<ShopDeskDO> {

    default PageResult<ShopDeskDO> selectPage(ShopDeskPageReqVO reqVO) {
        LambdaQueryWrapperX<ShopDeskDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(ShopDeskDO::getShopId,shopId);
        }
        wrapper.eqIfPresent(ShopDeskDO::getShopId, reqVO.getShopId())
                .likeIfPresent(ShopDeskDO::getShopName, reqVO.getShopName())
                .eqIfPresent(ShopDeskDO::getNumber, reqVO.getNumber())
                .eqIfPresent(ShopDeskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ShopDeskDO::getCateId,reqVO.getCateId())
                .orderByDesc(ShopDeskDO::getId);
        //超过两个小时当订单被判定为空闲
        if (OrderTypeEnum.TYPE_WORK.getValue().equals(reqVO.getType())){
            LocalDateTime nowTime = LocalDateTime.now();
            if(DeskStatusEnum.DESK_EMPTY.getValue().equals(reqVO.getDeskStatus())){
                wrapper.and(i->i.isNull(ShopDeskDO::getLastOrderTime).or().eq(ShopDeskDO::getLastOrderStatus,
                        OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue()));
            }else if(DeskStatusEnum.DESK_ING.getValue().equals(reqVO.getDeskStatus())){
                wrapper.eq(ShopDeskDO::getLastOrderStatus, OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue());
            }
        }

        return selectPage(reqVO, wrapper);
    }

    default List<ShopDeskDO> selectList(ShopDeskExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ShopDeskDO>()
                .eqIfPresent(ShopDeskDO::getShopId, reqVO.getShopId())
                .likeIfPresent(ShopDeskDO::getShopName, reqVO.getShopName())
                .eqIfPresent(ShopDeskDO::getNumber, reqVO.getNumber())
                .eqIfPresent(ShopDeskDO::getMiniQrcode, reqVO.getMiniQrcode())
                .eqIfPresent(ShopDeskDO::getH5Qrcode, reqVO.getH5Qrcode())
                .eqIfPresent(ShopDeskDO::getAliQrcode, reqVO.getAliQrcode())
                .eqIfPresent(ShopDeskDO::getNote, reqVO.getNote())
                .eqIfPresent(ShopDeskDO::getOrderCount, reqVO.getOrderCount())
                .eqIfPresent(ShopDeskDO::getCostAmount, reqVO.getCostAmount())
                .eqIfPresent(ShopDeskDO::getLastOrderNo, reqVO.getLastOrderNo())
                .betweenIfPresent(ShopDeskDO::getLastOrderTime, reqVO.getLastOrderTime())
                .eqIfPresent(ShopDeskDO::getLastOrderStatus, reqVO.getLastOrderStatus())
                .eqIfPresent(ShopDeskDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ShopDeskDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShopDeskDO::getId));
    }

}

package co.yixiang.yshop.module.merchant.service.order;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.OrderTypeEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.redis.util.redis.RedisUtil;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDeskVO;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.desk.enums.DeskStatusEnum;
import co.yixiang.yshop.module.member.convert.user.UserConvert;
import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.mysql.storeuser.StoreUserMapper;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppHomeVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppOrderCountVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppTodayDataVO;
import co.yixiang.yshop.module.merchant.enums.OrderStatusEnum;
import co.yixiang.yshop.module.merchant.service.home.AppHomeService;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.StoreOrderUpdateReqVO;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.convert.storeorder.StoreOrderConvert;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.order.enums.PayTypeEnum;
import co.yixiang.yshop.module.order.enums.UpdateOrderEnum;
import co.yixiang.yshop.module.order.service.storeorder.StoreOrderService;
import co.yixiang.yshop.module.order.service.storeorder.dto.StatusDto;
import co.yixiang.yshop.module.order.service.storeordercartinfo.StoreOrderCartInfoService;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static co.yixiang.yshop.framework.common.constant.ShopConstants.DAY_COUNT_KEY;
import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 会员卡 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppOrderServiceImpl implements AppOrderService {
    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Resource
    private StoreOrderCartInfoService storeOrderCartInfoService;
    @Resource
    private MemberUserService userService;
    @Resource
    private StoreOrderService storeOrderService;
    @Resource
    private ShopDeskMapper shopDeskMapper;

    /**
     * 订单列表
     *
     * @param shopId  店铺id
     * @param type  OrderStatusEnum
     * @param page  page
     * @param limit limit
     * @return list
     */
    @Override
    public List<AppStoreOrderQueryVo> orderList(Long shopId, String orderType,int type, int page, int limit,String key) {
        LambdaQueryWrapper<StoreOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderDO::getShopId, shopId);
        wrapper.eq(StoreOrderDO::getOrderType,orderType);
        wrapper.orderByDesc(StoreOrderDO::getId);
        if(StrUtil.isNotEmpty(key)){
            wrapper.and(i->i.eq(StoreOrderDO::getOrderId,key).or(j->j.eq(StoreOrderDO::getUserPhone,key)));
        }
        if(OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(orderType)){
            wrapper.eq(StoreOrderDO::getDueStatus,type);
        }else{
            switch (OrderStatusEnum.toType(type)) {
                //全部
                case STATUS__1:
                    break;
                //待出单
                case STATUS_0:
                    wrapper.eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue())
                            .and(i->i.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                                    .or(j->j.eq(StoreOrderDO::getOrderType, OrderLogEnum.ORDER_TAKE_DESK.getValue())
                                            .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())));
                    break;
                //待收货
                case STATUS_1:
                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_1.getValue());
                    break;
                //已完成
                case STATUS_2:
                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_3.getValue());
                    break;
                //待退款
                case STATUS_3:
                    wrapper.eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_1.getValue());
                    break;
                //待支付
                case STATUS_4:
                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())
                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                            .ge(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue());
                    break;
                //已退款
                case STATUS_5:
                    wrapper.eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_2.getValue());
                    break;
                default:
            }

        }

        Page<StoreOrderDO> pageModel = new Page<>(page, limit);
        IPage<StoreOrderDO> pageList = storeOrderMapper.selectPage(pageModel, wrapper);
        List<AppStoreOrderQueryVo> list = StoreOrderConvert.INSTANCE.convertList01(pageList.getRecords());

        return list.stream().map(this::handleOrder).collect(Collectors.toList());

    }


    @Override
    public void orderSend(Long id) {
       StoreOrderDO storeOrderDO =  storeOrderMapper.selectById(id);
       StoreOrderUpdateReqVO storeOrderUpdateReqVO = BeanUtils.toBean(storeOrderDO,StoreOrderUpdateReqVO.class);
       storeOrderUpdateReqVO.setUpdateType(UpdateOrderEnum.ORDER_SEND.getValue());
       storeOrderService.updateStoreOrder(storeOrderUpdateReqVO);
    }

    @Override
    public void takeStoreOrder(Long id) {
        storeOrderService.takeStoreOrder(id);
    }

    @Override
    public void orderRefund(Long id,String price) {
        //StoreOrderDO storeOrderDO =  storeOrderMapper.selectById(id);
        storeOrderService.orderRefund(id,new BigDecimal(price),0,null);
    }

    @Override
    public AppStoreOrderQueryVo orderDetail(Long id) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        AppStoreOrderQueryVo appStoreOrderQueryVo = StoreOrderConvert.INSTANCE.convert1(storeOrderDO);
        return handleOrder(appStoreOrderQueryVo);
    }

    public AppStoreOrderQueryVo handleOrder(AppStoreOrderQueryVo order) {
        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, order.getId());
        List<StoreOrderCartInfoDO> cartInfos = storeOrderCartInfoService.list(wrapper);

        order.setCartInfo(cartInfos);

        MemberUserDO user = userService.getUser(order.getUid());
        order.setAppUserInfoRespVO(UserConvert.INSTANCE.convert(user));


        if(ObjectUtil.isNotNull(order.getDeskId())){
            ShopDeskDO shopDeskDO = shopDeskMapper.selectById(order.getDeskId());
            order.setAppShopDeskVO(BeanUtils.toBean(shopDeskDO, AppShopDeskVO.class));
        }

        StatusDto statusDTO = new StatusDto();
        if (co.yixiang.yshop.module.order.enums.OrderStatusEnum.STATUS_0.getValue().equals(order.getPaid())) {
            //计算未支付到自动取消订 时间
            statusDTO.setYClass("nobuy");
            if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(order.getOrderType())){
                statusDTO.setType("1");
            }else {
                statusDTO.setType("0");
            }
            statusDTO.setTitle("未支付");
        } else if (OrderInfoEnum.REFUND_STATUS_1.getValue().equals(order.getRefundStatus())) {
            statusDTO.setYClass("state-sqtk");
            statusDTO.setMsg("商家审核中,请耐心等待");
            statusDTO.setType("-1");
            statusDTO.setTitle("申请退款中");
        } else if (OrderInfoEnum.REFUND_STATUS_2.getValue().equals(order.getRefundStatus())) {
            statusDTO.setYClass("state-sqtk");
            statusDTO.setMsg("已为您退款,感谢您的支持");
            statusDTO.setType("-2");
            statusDTO.setTitle("已退款");
        } else if (OrderInfoEnum.STATUS_0.getValue().equals(order.getStatus())) {
            statusDTO.setYClass("state-nfh");
            statusDTO.setMsg("商家未发货,请耐心等待");
            statusDTO.setType("1");
            statusDTO.setTitle("待出单");

        } else if (OrderInfoEnum.STATUS_1.getValue().equals(order.getStatus())) {
            if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(order.getOrderType())){
                statusDTO.setTitle("配送中");
            }else{
                statusDTO.setTitle("待取餐");
            }
            statusDTO.setYClass("state-ysh");
            statusDTO.setMsg("服务商已发货");
            statusDTO.setType("2");

        } else if (OrderInfoEnum.STATUS_2.getValue().equals(order.getStatus())) {
            if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(order.getOrderType())){
                statusDTO.setTitle("已收货");
            }else{
                statusDTO.setTitle("已取餐");
            }
            statusDTO.setYClass("state-ypj");
            statusDTO.setMsg("已收货,快去评价一下吧");
            statusDTO.setType("3");
        } else if (OrderInfoEnum.STATUS_3.getValue().equals(order.getStatus())) {
            statusDTO.setYClass("state-ytk");
            statusDTO.setMsg("交易完成,感谢您的支持");
            statusDTO.setType("4");
            statusDTO.setTitle("交易完成");
        }

        if (PayTypeEnum.WEIXIN.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("微信支付");
        } else if (PayTypeEnum.YUE.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("余额支付");
        } else if (PayTypeEnum.ALI.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("支付宝支付");
        }else {
            statusDTO.setPayType("积分支付");
        }

        order.setStatusDto(statusDTO);


        return order;
    }

}

package co.yixiang.yshop.module.merchant.service.storecart;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.HangOrderRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartSaveReqVO;
import co.yixiang.yshop.module.merchant.dal.dataobject.storecart.StoreCartDO;
import co.yixiang.yshop.module.merchant.dal.mysql.storecart.StoreCartMapper;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.StoreOrderUpdateReqVO;
import co.yixiang.yshop.module.order.controller.app.order.param.AppOrderParam;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.enums.PayTypeEnum;
import co.yixiang.yshop.module.order.enums.UpdateOrderEnum;
import co.yixiang.yshop.module.order.service.storeorder.AppStoreOrderService;
import co.yixiang.yshop.module.order.service.storeorder.AsyncStoreOrderService;
import co.yixiang.yshop.module.order.service.storeorder.StoreOrderService;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.product.service.storeproduct.dto.ProductFormatDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.ORDER_PAY_FINISH;

/**
 * 购物车 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreCartServiceImpl implements StoreCartService {

    @Resource
    private StoreCartMapper storeCartMapper;
    @Resource
    private AppStoreOrderService appStoreOrderService;
    @Resource
    private AsyncStoreOrderService asyncStoreOrderService;
    @Resource
    private StoreOrderService storeOrderService;

    @Value("${yshop.demo}")
    private Boolean isDemo;


    @Override
    public void addCart(StoreCartSaveReqVO reqVO) {
        StoreCartDO storeCartDO = storeCartMapper.selectOne(new LambdaQueryWrapper<StoreCartDO>()
                .eq(StoreCartDO::getProductId,reqVO.getProductId())
                .eq(StoreCartDO::getShopId,reqVO.getShopId())
                .eq(StoreCartDO::getIsHang,ShopCommonEnum.IS_STATUS_0.getValue())
                .eq(StoreCartDO::getProductAttrUnique,reqVO.getProductAttrUnique()));
        if(storeCartDO != null){
            storeCartDO.setCartNum(reqVO.getCartNum() + storeCartDO.getCartNum());
            storeCartMapper.updateById(storeCartDO);
        }else {
            storeCartMapper.insert(BeanUtils.toBean(reqVO, StoreCartDO.class));
        }

    }

    @Override
    public void updateCartNum(StoreCartSaveReqVO reqVO) {
        storeCartMapper.update(StoreCartDO.builder().cartNum(reqVO.getCartNum()).build(),
                new LambdaQueryWrapper<StoreCartDO>().eq(StoreCartDO::getId,reqVO.getId()));
    }

    @Override
    public void removeCart(Collection<Long> ids) {
        storeCartMapper.deleteBatchIds(ids);
    }

    @Override
    public void hangUpOrder(Collection<Long> ids) {
        String no =  "G" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
        storeCartMapper.update(StoreCartDO.builder().hangNo(no).isHang(ShopCommonEnum.IS_STATUS_1.getValue()).build(),
                new LambdaQueryWrapper<StoreCartDO>().in(StoreCartDO::getId,ids));
    }

    @Override
    public void hangOff(Long shopId, String hangNo) {
        storeCartMapper.update(StoreCartDO.builder().isHang(ShopCommonEnum.IS_STATUS_0.getValue()).build(),
                new LambdaQueryWrapper<StoreCartDO>()
                        .eq(StoreCartDO::getShopId,shopId).eq(StoreCartDO::getHangNo,hangNo));
    }

    @Override
    public void hangDel(Long shopId, String hangNo) {
        storeCartMapper.delete(new LambdaQueryWrapper<StoreCartDO>()
                        .eq(StoreCartDO::getShopId,shopId).eq(StoreCartDO::getHangNo,hangNo));
    }

    @Override
    public List<HangOrderRespVO> getHangList(Long shopId) {
        List<HangOrderRespVO> hangOrderRespVOS =  storeCartMapper.getHangList(shopId);
        for (HangOrderRespVO hangOrderRespVO : hangOrderRespVOS){
//            List<StoreCartDO> storeCartDOS = storeCartMapper.selectList(new LambdaQueryWrapper<StoreCartDO>()
//                    .eq(StoreCartDO::getHangNo,hangOrderRespVO.getHangNo()));
            List<StoreCartRespVO> storeCartRespVOList = storeCartMapper.selectJoinList(StoreCartRespVO.class,
                    new MPJLambdaWrapper<StoreCartDO>()
                    .selectAll(StoreCartDO.class)
                    .select(StoreProductDO::getStoreName)
                    .select(StoreProductAttrValueDO::getSku,StoreProductAttrValueDO::getImage,StoreProductAttrValueDO::getPrice)
                    .leftJoin(StoreProductDO.class,StoreProductDO::getId,StoreCartDO::getProductId)
                    .leftJoin(StoreProductAttrValueDO.class,StoreProductAttrValueDO::getUnique,StoreCartDO::getProductAttrUnique)
                    .eq(StoreCartDO::getHangNo,hangOrderRespVO.getHangNo()).isNotNull(StoreProductAttrValueDO::getUnique));
//            BigDecimal totalPrice = storeCartRespVOList.stream().map(StoreCartRespVO::getPrice)
//                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            BigDecimal totalPrice = storeCartRespVOList.stream()
                    .map(product -> product.getPrice().multiply(new BigDecimal(product.getCartNum()))) // 计算每个产品的总价（价格 * 数量）
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            hangOrderRespVO.setTotalPrice(totalPrice);
            hangOrderRespVO.setStoreCartRespVOS(storeCartRespVOList);



        }
        return hangOrderRespVOS;
    }

    @Override
    public List<StoreCartRespVO> getCartList(Long shopId) {
        return storeCartMapper.selectList(shopId);
    }


    @Override
    public Map<String, Object> submitOrder(AppOrderParam param) {
        param.setIsAdmin(true);
        if(PayTypeEnum.YUE.getValue().equals(param.getPayType()) && NumberUtil.compare(param.getUid(),0) <= 0){
            throw exception(new ErrorCode(202407150,"余额支付必须选择会员！"));
        }
        Long uid = 0L;
        if(param.getUid() != null){
            uid = param.getUid();
        }
        Map<String, Object> map = appStoreOrderService.createOrder(uid, param);
        //删除购物车
        if(!param.getCartIds().isEmpty()){
            storeCartMapper.delete(new LambdaQueryWrapper<StoreCartDO>().in(StoreCartDO::getId,param.getCartIds()));
        }

        return map;
    }

    @Override
    public Map<String, Object> updateOrder(AppOrderParam param) {
        StoreOrderDO storeOrderDO = appStoreOrderService.getById(param.getId());
        if(OrderInfoEnum.PAY_STATUS_1.getValue().equals(storeOrderDO.getPaid())){
            throw exception(new ErrorCode(202507251,"当前订单已经结账啦"));
        }
        if(PayTypeEnum.YUE.getValue().equals(param.getPayType()) && NumberUtil.compare(param.getUid(),0) <= 0){
            throw exception(new ErrorCode(202507252,"余额支付必须选择会员！"));
        }
        storeOrderDO.setUid(param.getUid());
        storeOrderDO.setPayPrice(param.getPayPrice());
        storeOrderDO.setDeductionPrice(param.getDeductionPrice());
        storeOrderDO.setPayType(param.getPayType());
        storeOrderDO.setRemark(param.getRemark());
        appStoreOrderService.updateById(storeOrderDO);
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",storeOrderDO.getOrderId());
        return map;
    }

    @Override
    public void printOrder(String orderId) {
        if(!isDemo){
            StoreOrderDO storeOrderDO = appStoreOrderService.getOne(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getOrderId,orderId));
            //出单且打印
//            StoreOrderUpdateReqVO updateReqVO = BeanUtils.toBean(storeOrderDO,StoreOrderUpdateReqVO.class);
//            updateReqVO.setUpdateType(UpdateOrderEnum.ORDER_SEND.getValue());
            storeOrderService.printOrder(storeOrderDO.getId());
        }
    }
}
package co.yixiang.yshop.module.order.service.storeordercartinfo;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.desk.enums.DueStatusEnum;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.mysql.user.MemberUserMapper;
import co.yixiang.yshop.module.order.dal.dataobject.storecartshare.StoreCartShareDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.storecartshare.StoreCartShareMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeordercartinfo.StoreOrderCartInfoMapper;
import co.yixiang.yshop.module.order.enums.OrderDueStatusEnum;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.product.controller.app.cart.vo.AppStoreCartQueryVo;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import co.yixiang.yshop.module.product.service.storeproductattrvalue.StoreProductAttrValueService;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import co.yixiang.yshop.module.system.api.user.dto.AdminUserRespDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单购物详情 Service 实现类
 *
 * @author yshop
 */
@Slf4j
@Service
@Validated
public class StoreOrderCartInfoServiceImpl extends ServiceImpl<StoreOrderCartInfoMapper, StoreOrderCartInfoDO> implements StoreOrderCartInfoService {

    @Resource
    private AppStoreProductService appStoreProductService;
    @Resource
    private StoreProductAttrValueService storeProductAttrValueService;
    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Resource
    private MemberUserMapper memberUserMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private StoreCartShareMapper storeCartShareMapper;
    @Resource
    private StoreOrderMapper storeOrderMapper;

    /**
     * 添加购物车商品信息
     * @param oid 订单id
     * @param orderId
     * @param productIds 商品id
     * @param numbers 商品数量
     * @param specs 商品规格
     */
    @Async
    @Override
    public void saveCartInfo(Long uid,String uidType,Long oid, String orderId, List<String> productIds,List<String> numbers,List<String> specs) {
        log.info("==========添加购物车商品信息start===========");

        int addProductMark = 0;
        //看看是否加餐
        List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = this.list(new LambdaQueryWrapper<StoreOrderCartInfoDO>()
                .eq(StoreOrderCartInfoDO::getOrderId,orderId)
                //.eq(StoreOrderCartInfoDO::getIsOrder, ShopCommonEnum.IS_STATUS_0.getValue())
                .orderByDesc(StoreOrderCartInfoDO::getAddProductMark));
        if(storeOrderCartInfoDOList != null && storeOrderCartInfoDOList.size() > 0){
            //只获取第一条
            StoreOrderCartInfoDO storeOrderCartInfoDO = storeOrderCartInfoDOList.get(0);
            addProductMark = storeOrderCartInfoDO.getAddProductMark() + 1;
        }
        String nickname = "";
        String avatar = "";
        //用户信息
//        if(ObjectUtil.isNotNull(uid) && NumberUtil.compare(uid,0) > 0){
//
        //}
        if(StrUtil.isNotEmpty(uidType) && uidType.equals("admin")){
            if(NumberUtil.compare(uid,0) == 0){
                uid = SecurityFrameworkUtils.getLoginUserId();
            }
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(uid);
            if(adminUserRespDTO == null){
                MemberUserDO memberUserDO = memberUserMapper.selectById(uid);
                nickname = memberUserDO.getNickname();
                avatar = memberUserDO.getAvatar();
            }else{
                nickname = adminUserRespDTO.getNickname();
                avatar = adminUserRespDTO.getAvatar();
            }
        }else{
            MemberUserDO memberUserDO = memberUserMapper.selectById(uid);
            nickname = memberUserDO.getNickname();
            avatar = memberUserDO.getAvatar();
        }

        List<StoreOrderCartInfoDO> list = new ArrayList<>();
        for (int i = 0;i < productIds.size();i++){
            String newSku = StrUtil.replace(specs.get(i),"|",",");
            StoreProductDO storeProductDO = appStoreProductService.getById(productIds.get(i));
            StoreProductAttrValueDO storeProductAttrValue = storeProductAttrValueService
                    .getOne(Wrappers.<StoreProductAttrValueDO>lambdaQuery()
                            .eq(StoreProductAttrValueDO::getSku, newSku)
                            .eq(StoreProductAttrValueDO::getProductId, productIds.get(i)));
            StoreOrderCartInfoDO info = new StoreOrderCartInfoDO();
            info.setUid(uid);
            info.setUAvatar(avatar);
            info.setUNickname(nickname);
            info.setUidType("user");
            if(StrUtil.isNotEmpty(uidType)){
                info.setUidType(uidType);
            }
            info.setAddTime(LocalDateTime.now());
            info.setOid(oid);
            info.setOrderId(orderId);
            info.setCartId(0L);
            info.setProductId(Long.valueOf(productIds.get(i)));
            info.setCartInfo("");
            info.setUnique(IdUtil.simpleUUID());
            info.setIsAfterSales(1);
            info.setTitle(storeProductDO.getStoreName());
            info.setImage(storeProductDO.getImage());
            info.setNumber(Integer.valueOf(numbers.get(i)));
            info.setSpec(newSku);
            info.setPrice(storeProductAttrValue.getPrice());
            info.setAddProductMark(addProductMark);
            list.add(info);
        }

        this.saveBatch(list);
    }


    /**
     * 更新餐桌信息
     * @param storeOrder 订单信息
     */
    @Override
    @Async
    public void updateDeskInfo(StoreOrderDO storeOrder) {
        ShopDeskDO shopDeskDO = shopDeskMapper.selectById(storeOrder.getDeskId());
        shopDeskDO.setLastOrderNo(storeOrder.getOrderId());
        shopDeskDO.setLastOrderTime(storeOrder.getCreateTime());
        shopDeskDO.setOrderCount(shopDeskDO.getOrderCount() + 1);
        shopDeskDO.setCostAmount(shopDeskDO.getCostAmount().add(storeOrder.getPayPrice()));
        shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue());
        //判断当前桌台是否还有预约订单
//        Long count = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
//                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
//                .eq(StoreOrderDO::getDeskId,storeOrder.getDeskId())
//                .eq(StoreOrderDO::getStatus, OrderDueStatusEnum.DUE_STATUS_1.getValue()));
//        if(count <= 1){
//            shopDeskDO.setBookStatus(ShopCommonEnum.IS_STATUS_1.getValue());
//            shopDeskDO.setBookTime(storeOrder.getDueTime());
//        }
        shopDeskDO.setBookStatus(ShopCommonEnum.IS_STATUS_0.getValue());
        //shopDeskDO.setBookTime(null);
        shopDeskMapper.updateById(shopDeskDO);

        //清空共享菜单
        //List<String> uids = StrUtil.split(storeOrder.getUserIds(),",");
        storeCartShareMapper.delete(new LambdaQueryWrapper<StoreCartShareDO>()
                .eq(StoreCartShareDO::getShopId,storeOrder.getShopId())
                .eq(StoreCartShareDO::getDeskId,storeOrder.getDeskId()));
    }
}

package co.yixiang.yshop.module.order.service.storeorder;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.UserTypeEnum;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.infra.api.websocket.WebSocketSenderApi;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserOrderCountVo;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.ShoperOrderTimeDataVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.ShopOrderMsgVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.UserCartMsgVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeordercartinfo.StoreOrderCartInfoMapper;
import co.yixiang.yshop.module.order.dal.redis.order.AsyncCountRedisDAO;
import co.yixiang.yshop.module.order.dal.redis.order.AsyncOrderRedisDAO;
import co.yixiang.yshop.module.order.dal.redis.order.PrintMechinRedisDAO;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.order.service.storeorder.dto.OrderTimeDataDto;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.service.storeproduct.StoreProductService;
import co.yixiang.yshop.module.store.controller.admin.webprint.vo.WebPrintSetVO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;
import co.yixiang.yshop.module.store.dal.dataobject.webprintconfig.WebPrintConfigDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import co.yixiang.yshop.module.store.enums.PrintBrandEnum;
import co.yixiang.yshop.module.store.service.webprint.WebPrintService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 异步订单 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class AsynStoreOrderServiceImpl implements AsyncStoreOrderService {

    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Resource
    private AsyncOrderRedisDAO asyncOrderRedisDAO;
    @Resource
    private AsyncCountRedisDAO asyncCountRedisDAO;
    @Resource
    private MemberUserService userService;
    @Resource
    private StoreProductService productService;
    @Resource
    private WebPrintService webPrintService;
    @Resource
    private PrintMechinRedisDAO printMechinRedisDAO;
    @Resource
    private StoreShopMapper storeShopMapper;
    @Resource
    private StoreOrderCartInfoMapper storeOrderCartInfoMapper;
    @Resource
    private WebSocketSenderApi webSocketSenderApi;

    /**
     * 获取某个用户的订单统计数据
     *
     * @param uid uid>0 取用户 否则取所有
     * @return
     */
    @Override
    @Async
    public void orderData(Long uid) {
        log.info("========获取某个用户的订单统计数据=========");
        //订单支付没有退款 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperOne = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperOne.eq(StoreOrderDO::getUid, uid);
        }
        wrapperOne.eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue());
        Long orderCount = storeOrderMapper.selectCount(wrapperOne);

        //订单支付没有退款 支付总金额
        double sumPrice = storeOrderMapper.sumPrice(uid);

        //订单待支付 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperTwo = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperTwo.eq(StoreOrderDO::getUid, uid);
        }
        wrapperTwo.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue());
        Long unpaidCount = storeOrderMapper.selectCount(wrapperTwo);

        //订单待发货 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperThree = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperThree.eq(StoreOrderDO::getUid, uid);
        }
        wrapperThree.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue());
        Long unshippedCount = storeOrderMapper.selectCount(wrapperThree);

        //订单待收货 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperFour = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperFour.eq(StoreOrderDO::getUid, uid);
        }
        wrapperFour.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_1.getValue());
        Long receivedCount = storeOrderMapper.selectCount(wrapperFour);

        //订单待评价 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperFive = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperFive.eq(StoreOrderDO::getUid, uid);
        }
        wrapperFive.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_2.getValue());
        Long evaluatedCount = storeOrderMapper.selectCount(wrapperFive);

        //订单已完成 数量
        LambdaQueryWrapper<StoreOrderDO> wrapperSix = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapperSix.eq(StoreOrderDO::getUid, uid);
        }
        wrapperSix.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_3.getValue());
        Long completeCount = storeOrderMapper.selectCount(wrapperSix);

        //售后退款
        Long salesCount = 0L;

         AppUserOrderCountVo appUserOrderCountVo = AppUserOrderCountVo.builder()
                .orderCount(orderCount)
                .sumPrice(sumPrice)
                .unpaidCount(unpaidCount)
                .unshippedCount(unshippedCount)
                .receivedCount(receivedCount)
                .evaluatedCount(evaluatedCount)
                .completeCount(completeCount)
                .refundCount(salesCount)
                .build();

         //存redis
        asyncOrderRedisDAO.set(appUserOrderCountVo,uid);

        this.getOrderTimeData();
    }


    /**
     * 首页订单/用户等统计
     *
     * @return OrderTimeDataDto
     */
    @Async
    @Override
    public void getOrderTimeData() {
        OrderTimeDataDto orderTimeDataDto = new OrderTimeDataDto();

        ShoperOrderTimeDataVo shoperOrderTimeData = this.getShoperOrderTimeData();

        BeanUtil.copyProperties(shoperOrderTimeData, orderTimeDataDto);

        Long shopId = 0L;
        if(SecurityFrameworkUtils.getLoginUser() != null){
            shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        }

        orderTimeDataDto.setUserCount(userService.count());
        orderTimeDataDto.setOrderCount(storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(shopId > 0,StoreOrderDO::getShopId,shopId)));
        orderTimeDataDto.setPriceCount(storeOrderMapper.sumTotalPrice(shopId));
        orderTimeDataDto.setGoodsCount(productService.count(new LambdaQueryWrapper<StoreProductDO>()
                .eq(shopId > 0,StoreProductDO::getShopId,shopId)));

        asyncCountRedisDAO.set(orderTimeDataDto);
    }

    /**
     * 异步后台统计
     */
    public ShoperOrderTimeDataVo getShoperOrderTimeData() {

        Date today = DateUtil.beginOfDay(new Date());
        Date yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        Date nowMonth = DateUtil.beginOfMonth(new Date());
        Date lastWeek = DateUtil.beginOfDay(DateUtil.lastWeek());

        ShoperOrderTimeDataVo orderTimeDataVo = new ShoperOrderTimeDataVo();
        Long shopId = 0L;
        if(SecurityFrameworkUtils.getLoginUser() != null){
            shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        }

        //今日成交额
        LambdaQueryWrapper<StoreOrderDO> wrapperOne = new LambdaQueryWrapper<>();
        wrapperOne
                .eq(shopId > 0,StoreOrderDO::getShopId,shopId)
                .ge(StoreOrderDO::getPayTime, today)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        orderTimeDataVo.setTodayPrice(storeOrderMapper.todayPrice(wrapperOne));
        //今日订单数
        orderTimeDataVo.setTodayCount(storeOrderMapper.selectCount(wrapperOne));

        //昨日成交额
        LambdaQueryWrapper<StoreOrderDO> wrapperTwo = new LambdaQueryWrapper<>();
        wrapperTwo
                .eq(shopId > 0,StoreOrderDO::getShopId,shopId)
                .lt(StoreOrderDO::getPayTime, today)
                .ge(StoreOrderDO::getPayTime, yesterday)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        orderTimeDataVo.setProPrice(storeOrderMapper.todayPrice(wrapperTwo));
        //昨日订单数
        orderTimeDataVo.setProCount(storeOrderMapper.selectCount(wrapperTwo));

        //本月成交额
        LambdaQueryWrapper<StoreOrderDO> wrapperThree = new LambdaQueryWrapper<>();
        wrapperThree
                .eq(shopId > 0,StoreOrderDO::getShopId,shopId)
                .ge(StoreOrderDO::getPayTime, nowMonth)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        orderTimeDataVo.setMonthPrice(storeOrderMapper.todayPrice(wrapperThree));
        //本月订单数
        orderTimeDataVo.setMonthCount(storeOrderMapper.selectCount(wrapperThree));

        //上周成交额
        LambdaQueryWrapper<StoreOrderDO> wrapperLastWeek = new LambdaQueryWrapper<>();
        wrapperLastWeek
                .eq(shopId > 0,StoreOrderDO::getShopId,shopId)
                .lt(StoreOrderDO::getPayTime, today)
                .ge(StoreOrderDO::getPayTime, lastWeek)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        orderTimeDataVo.setLastWeekPrice(storeOrderMapper.todayPrice(wrapperLastWeek));
        //上周订单数
        orderTimeDataVo.setLastWeekCount(storeOrderMapper.selectCount(wrapperLastWeek));


        return orderTimeDataVo;

    }



    /**
     * 异步打印
     * @param storeOrderDO
     */
    @Async
    @Override
    public void print(StoreOrderDO storeOrderDO){
        Map<String, List<String>> map = this.getMechineCode(storeOrderDO.getShopId());
        if(map.get(PrintBrandEnum.YLY.getValue()) != null){
            String temp = print(PrintBrandEnum.YLY.getValue(),storeOrderDO);
            List<String> ylyList = map.get(PrintBrandEnum.YLY.getValue());
            for(String code : ylyList){
                webPrintService.printOrder(code,temp,storeOrderDO.getOrderId());
            }

        }
        if(map.get(PrintBrandEnum.FEIE.getValue()) != null){
            String temp = print(PrintBrandEnum.FEIE.getValue(),storeOrderDO);
            List<String> feieList = map.get(PrintBrandEnum.FEIE.getValue());
            for(String code : feieList){
                webPrintService.feiePrintOrder(code,temp.toString());
            }

        }

    }

    /**
     * 拼装数据
     * @param brand 品牌
     * @param storeOrderDO 订单
     * @return 最终格式
     */
    public String print(String brand,StoreOrderDO storeOrderDO){
        WebPrintSetVO webPrintSetVO = webPrintService.getPrintConfig(brand);
        String temp = StrUtil.replace(webPrintSetVO.getTemplate(),"#storename#",storeOrderDO.getShopName());
        String formatPayTime = LocalDateTimeUtil.format(storeOrderDO.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN);
        temp = StrUtil.replace(temp,"#orderTime#",formatPayTime);
        temp = StrUtil.replace(temp,"#orderid#",storeOrderDO.getOrderId());
        temp = StrUtil.replace(temp,"#numberId#",storeOrderDO.getNumberId()+"");
        String deskNumber = "无";
        String deskPeople = "无";
        String address = "无";
        if(storeOrderDO.getOrderType().equals(OrderLogEnum.ORDER_TAKE_OUT.getValue())){
            address = storeOrderDO.getUserPhone() + " " + storeOrderDO.getUserAddress();
        }else if(storeOrderDO.getOrderType().equals(OrderLogEnum.ORDER_TAKE_IN.getValue())){
            address = "到店自取";
        } else{
            deskNumber = storeOrderDO.getDeskNumber();
            deskPeople = storeOrderDO.getDeskPeople().toString();
        }

        temp = StrUtil.replace(temp,"#deskNumber#",deskNumber);
        temp = StrUtil.replace(temp,"#deskPeople#",deskPeople);
        temp = StrUtil.replace(temp,"#address#",address);
        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid,storeOrderDO.getId());
        List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = storeOrderCartInfoMapper.selectList(wrapper);
        String goodsList = formatGood(brand,storeOrderCartInfoDOList,14, 6, 3, 6);
        temp = StrUtil.replace(temp,"#goodslList#",goodsList);
        temp = StrUtil.replace(temp,"#total#",storeOrderDO.getTotalPrice().toString());
        temp = StrUtil.replace(temp,"#couponPrice#",storeOrderDO.getCouponPrice().toString());
        temp = StrUtil.replace(temp,"#payPrice#",storeOrderDO.getPayPrice().toString());
        temp = StrUtil.replace(temp,"#notes#",storeOrderDO.getRemark());

        return temp;
    }

    @Async
    @Override
    public void pubCartInfo(UserCartMsgVo userCartMsgVo) {
        webSocketSenderApi.sendObject(UserTypeEnum.MEMBER.getValue(), "sync-menu-msg",userCartMsgVo);
    }

    @Override
    public void pubOrderInfo(ShopOrderMsgVo shopOrderMsgVo) {
        webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), "sync-order-msg",shopOrderMsgVo);
    }

    private Map<String, List<String>> getMechineCode(Long shopId){
        Map<String, List<String>> map = new LinkedHashMap<>();
        List<String> yly = new ArrayList<>();
        List<String> feie = new ArrayList<>();
        StoreShopDO storeShopDO = storeShopMapper.selectById(shopId);
        if(StrUtil.isNotEmpty(storeShopDO.getUniprintId())){
            WebPrintDO webPrintDO = webPrintService.getWebPrint(Long.valueOf(storeShopDO.getUniprintId()));
            if(webPrintDO != null){
                if(PrintBrandEnum.YLY.getValue().equals(webPrintDO.getBrand())){
                    yly.add(webPrintDO.getMechineCode());
                }else{
                    feie.add(webPrintDO.getMechineCode());
                }
            }
        }
        if(StrUtil.isNotEmpty(storeShopDO.getUserUniprintId())){
            WebPrintDO webPrintDO2 = webPrintService.getWebPrint(Long.valueOf(storeShopDO.getUserUniprintId()));
            if(webPrintDO2 != null){
                if(PrintBrandEnum.YLY.getValue().equals(webPrintDO2.getBrand())){
                    yly.add(webPrintDO2.getMechineCode());
                }else{
                    feie.add(webPrintDO2.getMechineCode());
                }
            }

        }

        if(StrUtil.isNotEmpty(storeShopDO.getKitchenUniprintId())){
            WebPrintDO webPrintDO3 = webPrintService.getWebPrint(Long.valueOf(storeShopDO.getKitchenUniprintId()));
            if(webPrintDO3 != null){
                if(PrintBrandEnum.YLY.getValue().equals(webPrintDO3.getBrand())){
                    yly.add(webPrintDO3.getMechineCode());
                }else{
                    feie.add(webPrintDO3.getMechineCode());
                }
            }

        }

        map.put(PrintBrandEnum.YLY.getValue(),yly);
        map.put(PrintBrandEnum.FEIE.getValue(),feie);

        return map;
    }



    /**
     * 排版格式化
     * @param brand 品牌
     * @param storeOrderCartInfoDOList 商品数据
     * @param b1 b1代表名称列占用（14个字节）
     * @param b2  b2单价列（6个字节
     * @param b3 b3数量列（3个字节
     * @param b4 b4金额列（6个字节）
     * @return
     */
    public  String formatGood(String brand,List<StoreOrderCartInfoDO> storeOrderCartInfoDOList, int b1, int b2, int b3, int b4) {
        int length = storeOrderCartInfoDOList.size();
        int addProductMark = storeOrderCartInfoDOList.get(length - 1).getAddProductMark();
        String orderInfo = "";
        String newLine = "\n";
        if(PrintBrandEnum.FEIE.getValue().equals(brand)){
            newLine = "<BR>";
        }
        orderInfo += "名称           单价  数量 金额" + newLine;
        double totals = 0.0;

        for(int i = 0; i <= addProductMark; i++ ){
            if(i == 0) {
                if(PrintBrandEnum.YLY.getValue().equals(brand)){
                    orderInfo += "<CA>首次点菜</CA>";
                }else{
                    orderInfo += "<C>首次点菜</C>";
                }
            }else{
                if(PrintBrandEnum.YLY.getValue().equals(brand)){
                    orderInfo += "<CA>第"+i+"次加菜</CA>";
                }else{
                    orderInfo += "<C>第"+i+"次加菜</C>";
                }
            }
            for (StoreOrderCartInfoDO storeOrderCartInfoDO : storeOrderCartInfoDOList) {
                String title = storeOrderCartInfoDO.getTitle();
                if(!"默认".equals(storeOrderCartInfoDO.getSpec())){
                       title = title + "(" + storeOrderCartInfoDO.getSpec() + ")";
                }
                String price = storeOrderCartInfoDO.getPrice().toString();
                String num = storeOrderCartInfoDO.getNumber().toString();
                String total = "" + Double.valueOf(price) * Integer.parseInt(num);
                totals += Double.parseDouble(total);
                price = addSpace(price, b2);
                num = addSpace(num, b3);
                total = addSpace(total, b4);
                String otherStr =  " " + price + num+ " " + total;
                if(i == storeOrderCartInfoDO.getAddProductMark()){
                    int tl = 0;
                    try {
                        tl = title.getBytes("GBK").length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    int spaceNum = (tl / b1 + 1) * b1 - tl;
                    if (tl < b1) {
                        for (int k = 0; k < spaceNum; k++) {
                            title += " ";
                        }
                        title += otherStr;
                    } else if (tl == b1) {
                        title += otherStr;
                    } else {
                        List<String> list = null;
                        if (isEn(title)) {
                            list = getStrList(title, b1);
                        } else {
                            list = getStrList(title, b1 / 2);
                        }
                        String s0 = titleAddSpace(list.get(0));
                        title = s0 + otherStr  + newLine;// 添加 单价 数量 总额
                        String s = "";
                        for (int k = 1; k < list.size(); k++) {
                            s += list.get(k);
                        }
                        try {
                            s = getStringByEnter(b1, s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        title += s;
                    }
                    orderInfo += title + newLine;
                }
            }
        }

        return orderInfo;
    }

    public static String getStringByEnter(int length, String string) throws Exception {
        for (int i = 1; i <= string.length(); i++) {
            if (string.substring(0, i).getBytes("GBK").length > length) {
                return string.substring(0, i - 1) + "<BR>" + getStringByEnter(length, string.substring(i - 1));
            }
        }
        return string;
    }

    public String titleAddSpace(String str) {
        int k=0;
        int b = 14;
        try {
            k = str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < b-k; i++) {
            str += " ";
        }
        return str;
    }

    public static String addSpace(String str, int size) {
        int len = str.length();
        if (len < size) {
            for (int i = 0; i < size - len; i++) {
                str += " ";
            }
        }
        return str;
    }

    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static Boolean isEn(String str) {
        Boolean b = false;
        try {
            b = str.getBytes("GBK").length == str.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return b;
    }


}

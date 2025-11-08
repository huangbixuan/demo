package co.yixiang.yshop.module.desk.service.shopdesk;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.desk.dal.mysql.shopdeskcategory.ShopDeskCategoryMapper;
import co.yixiang.yshop.module.desk.enums.DeskStatusEnum;
import co.yixiang.yshop.module.desk.enums.DueStatusEnum;
import co.yixiang.yshop.module.desk.service.qrcode.QrcodeService;
import co.yixiang.yshop.module.order.api.OrderApi;
import co.yixiang.yshop.module.order.api.dto.AppShopDueDTO;
import co.yixiang.yshop.module.order.api.dto.AppStoreOrderDTO;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import co.yixiang.yshop.module.store.dal.mysql.shopduelabel.ShopDueLabelMapper;
import co.yixiang.yshop.module.store.dal.mysql.shopduerule.ShopDueRuleMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 门店 - 桌号 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppShopDeskServiceImpl implements AppShopDeskService {

    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private ShopDeskCategoryMapper shopDeskCategoryMapper;
    @Resource
    private ShopDueRuleMapper shopDueRuleMapper;
    @Resource
    private OrderApi orderApi;
    @Resource
    private ShopDueLabelMapper shopDueLabelMapper;


    @Override
    public List<AppDeskDueVO> getList(Long shopId,String deskStatus) {
        List<ShopDeskCategoryDO> categoryDOS = shopDeskCategoryMapper
                .selectList(new LambdaQueryWrapper<ShopDeskCategoryDO>()
                .eq(ShopDeskCategoryDO::getShopId,shopId)
                .eq(ShopDeskCategoryDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(ShopDeskCategoryDO::getSort));
        List<AppDeskDueVO> appDeskDueVOS = BeanUtils.toBean(categoryDOS,AppDeskDueVO.class);
        LocalDateTime nowTime = LocalDateTime.now();


        for (AppDeskDueVO d : appDeskDueVOS){
            LambdaQueryWrapper<ShopDeskDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShopDeskDO::getCateId,d.getId())
                    .eq(ShopDeskDO::getStatus,ShopCommonEnum.IS_STATUS_1.getValue());
            //空闲或者就餐中
            if(DeskStatusEnum.DESK_EMPTY.getValue().equals(deskStatus)){
                wrapper.and(i->i.isNull(ShopDeskDO::getLastOrderTime).or().eq(ShopDeskDO::getLastOrderStatus,
                        OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue()));
//                wrapper.and(i->i.lt(ShopDeskDO::getLastOrderTime,nowTime.minusHours(2))
//                        .or().isNull(ShopDeskDO::getLastOrderTime).or().eq(ShopDeskDO::getLastOrderStatus,
//                                OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue()));
            }else if(DeskStatusEnum.DESK_ING.getValue().equals(deskStatus)){
                wrapper.eq(ShopDeskDO::getLastOrderStatus, OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue());
            }
            List<ShopDeskDO> shopDeskDOS = shopDeskMapper.selectList(wrapper);
            List<AppShopDeskVO> appShopDeskVOS = BeanUtils.toBean(shopDeskDOS,AppShopDeskVO.class);
            d.setChildrens(appShopDeskVOS);
        }


        return appDeskDueVOS;
    }


    @Override
    public AppShopDeskVO getDetail(Long deskId) {
        ShopDeskDO shopDeskDO = shopDeskMapper.selectById(deskId);
        AppShopDeskVO appShopDeskVO = BeanUtils.toBean(shopDeskDO,AppShopDeskVO.class);
        List<DayTime> dayTimes = new ArrayList<>();
        //可以预定七天
        for (int i = 0;i < 7;i++){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime localDateTime = LocalDateTimeUtil.offset(now, i, ChronoUnit.DAYS);
            DayTime dayTime = new DayTime();
            if(i == 0){
                dayTime.setDayStr("今天");
            }else if(i == 1){
                dayTime.setDayStr("明天");
            }else{
                String str = LocalDateTimeUtil.format(localDateTime,"MM月dd日");
                dayTime.setDayStr(str);
            }

            List<ShopDueRuleDO> dueRuleDOS = shopDueRuleMapper.selectList(new LambdaQueryWrapper<ShopDueRuleDO>()
                    .eq(ShopDueRuleDO::getShopId,shopDeskDO.getShopId()).orderByAsc(ShopDueRuleDO::getStartTime));
            List<TimeRule> timeRules = new ArrayList<>();
            for (ShopDueRuleDO shopDueRuleDO : dueRuleDOS){
                ShopDueLabelDO shopDueLabelDO = shopDueLabelMapper.selectById(shopDueRuleDO.getLabelId());
                Date startTimeD = DateUtil.parse(shopDueRuleDO.getStartTime());
                Date endTimeD = DateUtil.parse(shopDueRuleDO.getEndTime());
                LocalDateTime startTime = LocalDateTimeUtil.of(startTimeD);
                LocalDateTime endTime = LocalDateTimeUtil.of(endTimeD);

                LocalDateTime current = startTime;

                while (current.compareTo(endTime) <=0){
                    TimeRule timeRule = new TimeRule();
                    String currentTimeStr = LocalDateTimeUtil.format(current,"HH:mm");
                    Integer status = DueStatusEnum.STATUS_0.getValue();

                    LocalDateTime currentOff = LocalDateTimeUtil.offset(current, i, ChronoUnit.DAYS);
                    if(currentOff.compareTo(now) < 0){
                        status = DueStatusEnum.STATUS_2.getValue();
                    }
                    AppStoreOrderDTO appStoreOrderDTO = orderApi.getDueOrderInfo(deskId,currentOff);
                    if(appStoreOrderDTO != null){
                        status = DueStatusEnum.STATUS_1.getValue();

                    }
                    if(!status.equals(DueStatusEnum.STATUS_2.getValue())){
                        timeRule.setStatus(status);
                        timeRule.setTag(shopDueLabelDO.getTitle());
                        timeRule.setTime(currentTimeStr);
                        timeRule.setTimeDate(currentOff);
                        timeRules.add(timeRule);
                    }

                    current = current.plusHours(shopDueRuleDO.getInterval().longValue());

                }
            }

            dayTime.setTimeRule(timeRules);

            dayTimes.add(dayTime);
        }
        appShopDeskVO.setDayTime(dayTimes);
        return appShopDeskVO;
    }

    @Override
    public AppShopDeskVO getDesk(Long deskId) {
        ShopDeskDO shopDeskDO = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                .eq(ShopDeskDO::getLastOrderStatus,OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue())
                .eq(ShopDeskDO::getId,deskId));
        if(shopDeskDO == null) {
            return null;
        }
        AppShopDeskVO appShopDeskVO = BeanUtils.toBean(shopDeskDO,AppShopDeskVO.class);
        return appShopDeskVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createDueOrder(Long uid,AppShopDueParam param) {
        String orderId =  orderApi.addDueOrder(AppShopDueDTO.builder()
                        .uid(uid)
                        .deskId(param.getDeskId())
                        .dueTime(param.getDueTime())
                        .deskPeople(param.getDeskPeople())
                        .reachTime(param.getReachTime())
                        .userPhone(param.getUserPhone())
                        .realName(param.getRealName())
                        .build());

        //更新桌面信息预约状态与与预约时间
        //查询桌面就餐信息
        ShopDeskDO shopDeskDO = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                .eq(ShopDeskDO::getId,param.getDeskId()));
        if(OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue().equals(shopDeskDO.getLastOrderStatus())){
            shopDeskMapper.update(new ShopDeskDO().setBookTime(param.getDueTime())
                    .setLastOrderNo(orderId)
                    .setBookStatus(ShopCommonEnum.IS_STATUS_1.getValue()),new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getId,param.getDeskId()));
        }


        return orderId;
    }


}

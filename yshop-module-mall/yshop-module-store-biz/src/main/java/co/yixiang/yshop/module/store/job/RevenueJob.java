package co.yixiang.yshop.module.store.job;

import co.yixiang.yshop.framework.common.enums.OrderTypeEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.quartz.core.handler.JobHandler;
import co.yixiang.yshop.framework.tenant.core.aop.TenantIgnore;
import co.yixiang.yshop.framework.tenant.core.job.TenantJob;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.mysql.storerevenue.StoreRevenueMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺收入结算定时任务每天晚上12点执行
 */
@Component
public class RevenueJob implements JobHandler {

    @Resource
    private StoreRevenueMapper storeRevenueMapper;
    @Resource
    private StoreShopMapper storeShopMapper;

    @Override
    @TenantJob // 标记多租户
    //@TenantIgnore
    public String execute(String param) throws Exception {
        System.out.println("结算定时任务start================");

        storeRevenueMapper.selectList(new LambdaQueryWrapper<StoreRevenueDO>()
                .eq(StoreRevenueDO::getOrderType, OrderTypeEnum.COMMON_ORDER.getValue())
                .eq(StoreRevenueDO::getIsFinish, ShopCommonEnum.IS_FINISH_0.getValue()), new ResultHandler<StoreRevenueDO>() {
            @Override
            public void handleResult(ResultContext<? extends StoreRevenueDO> resultContext) {
                StoreRevenueDO storeRevenueDO = resultContext.getResultObject();
                Integer isFinish = ShopCommonEnum.IS_FINISH_1.getValue();
                if(ShopCommonEnum.ADD_1.getValue().equals(storeRevenueDO.getType())){
                    //更新金额
                    storeShopMapper.incMoney(storeRevenueDO.getShopId(),storeRevenueDO.getAmount());
                }else {//减去余额
                    int count = storeShopMapper.decMoney(storeRevenueDO.getShopId(),storeRevenueDO.getAmount());
                    if(count == 0){
                        //扣款失败
                        isFinish = ShopCommonEnum.IS_FINISH_0.getValue();
                    }
                }
                //状态更新
                storeRevenueDO.setIsFinish(isFinish);
                storeRevenueMapper.updateById(storeRevenueDO);
            }
        });

        return "==== 店铺结算更新数量end====";
    }

}

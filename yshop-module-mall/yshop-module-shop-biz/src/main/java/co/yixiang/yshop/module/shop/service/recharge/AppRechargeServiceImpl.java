package co.yixiang.yshop.module.shop.service.recharge;

import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.module.shop.dal.mysql.recharge.RechargeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 充值金额管理 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppRechargeServiceImpl extends ServiceImpl<RechargeMapper, RechargeDO> implements AppRechargeService {

}

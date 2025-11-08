package co.yixiang.yshop.module.mp.api;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.module.mp.api.user.MpUserApi;
import co.yixiang.yshop.module.mp.dal.dataobject.user.MpUserDO;
import co.yixiang.yshop.module.mp.dal.mysql.user.MpUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpUserApiImpl implements MpUserApi {
    @Resource
    private MpUserMapper mpUserMapper;
    @Override
    public List<String> getOpenIds() {
        List<MpUserDO> mpUserDOList = mpUserMapper.selectList(new LambdaQueryWrapper<MpUserDO>()
                .eq(MpUserDO::getIsAdminNotice, ShopCommonEnum.YES.getValue()));
        return mpUserDOList.stream().map(MpUserDO::getOpenid).collect(Collectors.toList());
    }
}

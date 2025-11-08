package co.yixiang.yshop.module.message.supply;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.message.dal.dataobject.wechattemplate.WechatTemplateDO;
import co.yixiang.yshop.module.message.enums.WechatTempateEnum;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import co.yixiang.yshop.module.message.service.wechattemplate.WechatTemplateService;
import co.yixiang.yshop.module.mp.service.account.MpAccountService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;

;

/**
 * 小程序订阅消息通知
 */
@Service
public class WeiXinSubscribeService {

    @Resource
    private MemberUserService userService;
    @Resource
    private WechatTemplateService wechatTemplateService;
    @Resource
    private MpAccountService mpAccountService;




    /**
     * 发货成功通知
     *
     * @param weixinNoticeMessage 数据
     */
    public void deliverySuccessNotice(WeixinNoticeMessage weixinNoticeMessage) {

        String openid = this.getUserOpenid(weixinNoticeMessage.getUid());

        if (StrUtil.isEmpty(openid)) {
            return;
        }
        //OrderDTO orderDTO = productOrderApi.getOrderInfo(weixinNoticeMessage.getOrderId());
       // System.out.println("orderDTO:"+ orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("character_string7", weixinNoticeMessage.getOrderId());
        map.put("phrase12", "已经发货");
        map.put("thing1", weixinNoticeMessage.getDeliveryName());
        map.put("character_string2", weixinNoticeMessage.getDeliveryId());
        String tempId = this.getTempId(WechatTempateEnum.DELIVERY_SUCCESS.getValue());
        if (StrUtil.isNotBlank(tempId)) {
            this.sendSubscribeMsg(openid, tempId, "orderInfo/orderInfo?details=orderId=" + weixinNoticeMessage.getOrderId(), map);
        }
    }


    /**
     * 构建小程序一次性订阅消息
     * @param openId 单号
     * @param templateId 模板id
     * @param page 跳转页面
     * @param map map内容
     * @return String
     */
    private void sendSubscribeMsg(String openId, String templateId, String page, Map<String,String> map){
        WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage.builder()
                .toUser(openId)
                .templateId(templateId)
                .page(page)
                .build();
        map.forEach( (k,v)-> { wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.MsgData(k, v));} );
        try {
            WxMaService wxMaService = mpAccountService.getMaService();
            wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取模板消息id
     *
     * @param key 模板key
     * @return string
     */
    private String getTempId(String key) {
        WechatTemplateDO yxWechatTemplate = wechatTemplateService.lambdaQuery()
                .eq(WechatTemplateDO::getType, "subscribe")
                .eq(WechatTemplateDO::getTempkey, key)
                .one();
        if (yxWechatTemplate == null) {
            throw exception(new ErrorCode(9999999, "请后台配置key:" + key + "订阅消息id"));
        }
        return yxWechatTemplate.getTempid();
    }


    /**
     * 获取openid
     *
     * @param uid uid
     * @return String
     */
    private String getUserOpenid(Long uid) {
        MemberUserDO yxUser = userService.getById(uid);
        if (yxUser == null) {
            return "";
        }
        if (StrUtil.isBlank(yxUser.getRoutineOpenid())) {
            return "";
        }
        return yxUser.getRoutineOpenid();

    }
}

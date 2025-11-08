/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.message.supply;

import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.tenant.core.util.TenantUtils;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.message.dal.dataobject.wechattemplate.WechatTemplateDO;
import co.yixiang.yshop.module.message.enums.WechatTempateEnum;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import co.yixiang.yshop.module.message.service.wechattemplate.WechatTemplateService;
import co.yixiang.yshop.module.mp.api.user.MpUserApi;
import co.yixiang.yshop.module.mp.service.account.MpAccountService;
import co.yixiang.yshop.module.order.api.OrderApi;
import co.yixiang.yshop.module.order.api.dto.AppStoreOrderDTO;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @ClassName 微信公众号模板通知
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/27
 **/
@Slf4j
@Service
public class WeixinTemplateService {


    @Resource
    private MemberUserService userService;
    @Resource
    private WechatTemplateService wechatTemplateService;
    @Resource
    private MpAccountService mpAccountService;

    @Resource
    private MpUserApi mpUserApi;
    @Resource
    private OrderApi orderApi;




    /**
     * 支付成功通知
     *
     * @param weixinNoticeMessage 数据
     */
    public void newOrderNotice(WeixinNoticeMessage weixinNoticeMessage) {
        TenantUtils.executeIgnore(()->{
            List<String> stringList = mpUserApi.getOpenIds();
            AppStoreOrderDTO appStoreOrderDTO = orderApi.getOrderInfo(weixinNoticeMessage.getOrderId());
            System.out.println("appStoreOrderDTO:"+appStoreOrderDTO);
            String goodsName = "";
            if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(appStoreOrderDTO.getOrderType())){
                goodsName = "堂食订单,桌号:" + appStoreOrderDTO.getDeskNumber() + ",人数:"+ appStoreOrderDTO.getDeskPeople();
            } else if (OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(appStoreOrderDTO.getOrderType())) {
                goodsName = "外卖订单,取餐号:" + appStoreOrderDTO.getNumberId();
            }else if (OrderLogEnum.ORDER_TAKE_IN.getValue().equals(appStoreOrderDTO.getOrderType())) {
                goodsName = "自取订单,取餐号:" + appStoreOrderDTO.getNumberId();
            }else if (OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(appStoreOrderDTO.getOrderType())) {
                goodsName = "预约订单,桌号:" + appStoreOrderDTO.getDeskNumber();
            }

            for (String openid : stringList){
                if (StrUtil.isBlank(openid)) {
                    continue;
                }
                System.out.println("goodsName:"+goodsName);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Map<String, String> map = new HashMap<>();
                map.put("thing16", appStoreOrderDTO.getShopName());
                map.put("character_string1", appStoreOrderDTO.getOrderId());
                map.put("thing8", goodsName);
                map.put("time2", simpleDateFormat.format(new Date()));
                String tempId = this.getTempId(WechatTempateEnum.NEW_ORDER_NOTICE.getValue());
                if (StrUtil.isNotBlank(tempId)) {
                    this.sendWxMpTemplateMessage(openid, tempId, "#", map);
                }
            }
        });


    }



    /**
     * 构建微信模板通知
     * @param openId 单号
     * @param templateId 模板id
     * @param url 跳转url
     * @param map map内容
     * @return String
     */
    private String sendWxMpTemplateMessage(String openId, String templateId, String url, Map<String,String> map){
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(templateId)
                .url(url)
                .build();
        map.forEach( (k,v)-> { templateMessage.addData(new WxMpTemplateData(k, v, "#000000"));} );
        String msgId = null;
        try {
            WxMpService wxMpService = mpAccountService.getWechatService();
            msgId =   wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return msgId;
    }


//    /**
//     * 构建微信模板通知
//     *
//     * @param openId     单号
//     * @param templateId 模板id
//     * @param url        跳转url
//     * @param map        map内容
//     * @return String
//     */
//    private String sendWxMpTemplateMessage(String openId, String templateId, String url, Map<String, String> map) {
//        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
//                .toUser(openId)
//                .templateId(templateId)
//                .url(url)
//                .build();
//        map.forEach((k, v) -> {
//            templateMessage.addData(new WxMpTemplateData(k, v, "#000000"));
//        });
//        String msgId = null;
//        try {
//            msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//        return msgId;
//    }


    /**
     * 获取模板消息id
     *
     * @param key 模板key
     * @return string
     */
    private String getTempId(String key) {
        WechatTemplateDO yxWechatTemplate = wechatTemplateService.lambdaQuery()
                .eq(WechatTemplateDO::getType, "template")
                .eq(WechatTemplateDO::getTempkey, key)
                .one();
        if (yxWechatTemplate == null) {
            throw exception(new ErrorCode(9999999, "请后台配置key:" + key + "订阅消息id"));
        }

        if (ShopCommonEnum.NO.getValue().equals(yxWechatTemplate.getStatus())) {
            return "";
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

        if (StrUtil.isBlank(yxUser.getOpenid())) {
            return "";
        }
        return yxUser.getOpenid();

    }


}

package co.yixiang.yshop.module.pay.service.tramsfer;

import co.yixiang.yshop.framework.common.enums.PayIdEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.pay.dal.dataobject.merchantdetails.MerchantDetailsDO;
import co.yixiang.yshop.module.pay.service.dto.TransferBillEntity;
import co.yixiang.yshop.module.pay.service.dto.TransferSceneReportInfo;
import co.yixiang.yshop.module.pay.service.dto.TransferToUserRequest;
import co.yixiang.yshop.module.pay.service.dto.TransferToUserResponse;
import co.yixiang.yshop.module.pay.service.merchantdetails.MerchantDetailsService;
import co.yixiang.yshop.module.pay.utils.WXPayUtility;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class TransferToUserService {
    private static String HOST = "https://api.mch.weixin.qq.com";
    private static String METHOD = "POST";
    private static String PATH = "/v3/fund-app/mch-transfer/transfer-bills";

    private static String METHOD2 = "GET";
    private static String PATH2 = "/v3/fund-app/mch-transfer/transfer-bills/out-bill-no/{out_bill_no}";


    @Resource
    private MerchantDetailsService merchantDetailsService;


    /**
     * 查询转账订单
     * @param outBillNo 商户单号
     * @return
     */
    public TransferBillEntity transferBill(String outBillNo) {
        Long tenantId = TenantContextHolder.getTenantId();
        MerchantDetailsDO merchantDetailsDO = merchantDetailsService
                .getMerchantDetails(PayIdEnum.WX_MINIAPP.getValue() + tenantId);
        if (merchantDetailsDO == null) {
            throw exception(new ErrorCode(202510282, "未配置微信小程序支付信息"));
        }
        try {
            TransferBillEntity response = run2(outBillNo,merchantDetailsDO);
            System.out.println("response:"+response);
            return response;
        } catch (WXPayUtility.ApiException e) {
            e.printStackTrace();
            throw exception(new ErrorCode(202510281,e.getMessage()));
        }
    }

    public TransferBillEntity run2(String outBillNo,MerchantDetailsDO merchantDetailsDO) {
        String uri = PATH2;
        uri = uri.replace("{out_bill_no}", WXPayUtility.urlEncode(outBillNo));

        Request.Builder reqBuilder = new Request.Builder().url(HOST + uri);
        reqBuilder.addHeader("Accept", "application/json");
        reqBuilder.addHeader("Wechatpay-Serial", merchantDetailsDO.getWechatPayPublicKeyId());
        reqBuilder.addHeader("Authorization", WXPayUtility.buildAuthorization(merchantDetailsDO.getMchId(),
                merchantDetailsDO.getCertificateSerialNo(),
                WXPayUtility.loadPrivateKeyFromPath(merchantDetailsDO.getPrivateKey()),
                METHOD2, uri, null));
        reqBuilder.method(METHOD2, null);
        Request httpRequest = reqBuilder.build();

        // 发送HTTP请求
        OkHttpClient client = new OkHttpClient.Builder().build();
        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            String respBody = WXPayUtility.extractBody(httpResponse);
            if (httpResponse.code() >= 200 && httpResponse.code() < 300) {
                // 2XX 成功，验证应答签名
                WXPayUtility.validateResponse(merchantDetailsDO.getWechatPayPublicKeyId(),
                        WXPayUtility.loadPublicKeyFromPath(merchantDetailsDO.getWechatPayPublicKey()),
                        httpResponse.headers(), respBody);

                // 从HTTP应答报文构建返回数据
                return WXPayUtility.fromJson(respBody, TransferBillEntity.class);
            } else {
                throw new WXPayUtility.ApiException(httpResponse.code(), respBody, httpResponse.headers());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Sending request to " + uri + " failed.", e);
        }
    }

    /**
     * 转账到用户
     * @param outBillNo 商户单号
     * @param amount 金额
     * @param openid openID
     * @return
     */
    public TransferToUserResponse startTransfer(String outBillNo, BigDecimal amount,String openid) {
        // TODO: 请准备商户开发必要参数，参考：https://pay.weixin.qq.com/doc/v3/merchant/4013070756
//        TransferToUserService client = new TransferToUserService(
//                "",                    // 商户号，是由微信支付系统生成并分配给每个商户的唯一标识符，商户号获取方式参考 https://pay.weixin.qq.com/doc/v3/merchant/4013070756
//                "",         // 商户API证书序列号，如何获取请参考 https://pay.weixin.qq.com/doc/v3/merchant/4013053053
//                "",     // 商户API证书私钥文件路径，本地文件路径
//                "",      // 微信支付公钥ID，如何获取请参考 https://pay.weixin.qq.com/doc/v3/merchant/4013038816
//                ""           // 微信支付公钥文件路径，本地文件路径
//        );
        Long tenantId = TenantContextHolder.getTenantId();
        MerchantDetailsDO merchantDetailsDO = merchantDetailsService.getMerchantDetails(PayIdEnum.WX_MINIAPP.getValue() + tenantId);
        if (merchantDetailsDO == null) {
            throw exception(new ErrorCode(202510280, "未配置微信小程序支付信息"));
        }

        TransferToUserRequest request = new TransferToUserRequest();
        request.setAppid(merchantDetailsDO.getAppid());
        request.setOutBillNo(outBillNo);
        request.setTransferSceneId("1005");
        request.setOpenid(openid);
       // request.userName = client.encrypt("");
        BigDecimal newAmount = amount.multiply(new BigDecimal(100));
        request.setTransferAmount(newAmount.longValue());
        request.setTransferRemark("佣金");
        request.setUserRecvPerception("");
        List<TransferSceneReportInfo> transferSceneReportInfos = new ArrayList<>();
        TransferSceneReportInfo transferSceneReportInfosItem0 = new TransferSceneReportInfo();
        transferSceneReportInfosItem0.setInfoType("岗位类型");
        transferSceneReportInfosItem0.setInfoContent("店长");
        transferSceneReportInfos.add(transferSceneReportInfosItem0);
        TransferSceneReportInfo transferSceneReportInfosItem1 = new TransferSceneReportInfo();
        transferSceneReportInfosItem1.setInfoType("报酬说明");
        transferSceneReportInfosItem1.setInfoContent("月底奖励");
        transferSceneReportInfos.add(transferSceneReportInfosItem1);
        request.setTransferSceneReportInfos(transferSceneReportInfos);

        try {
            TransferToUserResponse response = run(request,merchantDetailsDO);
            return response;
        } catch (WXPayUtility.ApiException e) {
            e.printStackTrace();
            throw exception(new ErrorCode(202510271,e.getMessage()));
        }
    }


    public TransferToUserResponse run(TransferToUserRequest request, MerchantDetailsDO merchantDetailsDO) {
        String uri = PATH;
        String reqBody = WXPayUtility.toJson(request);

        Request.Builder reqBuilder = new Request.Builder().url(HOST + uri);
        reqBuilder.addHeader("Accept", "application/json");
        reqBuilder.addHeader("Wechatpay-Serial", merchantDetailsDO.getWechatPayPublicKeyId());
        reqBuilder.addHeader("Authorization", WXPayUtility.buildAuthorization(merchantDetailsDO.getMchId(),
                merchantDetailsDO.getCertificateSerialNo(),
                WXPayUtility.loadPrivateKeyFromPath(merchantDetailsDO.getPrivateKey()),
                METHOD, uri, reqBody));
        reqBuilder.addHeader("Content-Type", "application/json");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), reqBody);
        reqBuilder.method(METHOD, requestBody);
        Request httpRequest = reqBuilder.build();

        // 发送HTTP请求
        OkHttpClient client = new OkHttpClient.Builder().build();
        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            String respBody = WXPayUtility.extractBody(httpResponse);
            if (httpResponse.code() >= 200 && httpResponse.code() < 300) {
                // 2XX 成功，验证应答签名
                WXPayUtility.validateResponse(merchantDetailsDO.getWechatPayPublicKeyId(),
                        WXPayUtility.loadPublicKeyFromPath(merchantDetailsDO.getWechatPayPublicKey()),
                        httpResponse.headers(), respBody);

                // 从HTTP应答报文构建返回数据
                return WXPayUtility.fromJson(respBody, TransferToUserResponse.class);
            } else {
                throw new WXPayUtility.ApiException(httpResponse.code(), respBody, httpResponse.headers());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Sending request to " + uri + " failed.", e);
        }
    }



    public String encrypt(String plainText) {
        //return WXPayUtility.encrypt(this.wechatPayPublicKey, plainText);
        return "";
    }

}

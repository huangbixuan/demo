package co.yixiang.yshop.module.store.service.webprint;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.redis.util.redis.RedisUtil;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.store.controller.admin.webprint.vo.*;
import co.yixiang.yshop.module.store.convert.webprint.WebPrintConvert;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;
import co.yixiang.yshop.module.store.dal.dataobject.webprintconfig.WebPrintConfigDO;
import co.yixiang.yshop.module.store.dal.mysql.webprint.WebPrintMapper;
import co.yixiang.yshop.module.store.dal.mysql.webprintconfig.WebPrintConfigMapper;
import co.yixiang.yshop.module.store.dal.redis.PrintTokenRedisDAO;
import co.yixiang.yshop.module.store.enums.PrintBrandEnum;
import co.yixiang.yshop.module.store.service.webprint.dto.ResponseData;
import co.yixiang.yshop.module.store.service.webprint.dto.TokeData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yly.java.yly_sdk.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.WEB_PRINT_NOT_EXISTS;

/**
 * 易联云打印机 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class WebPrintServiceImpl implements WebPrintService {

    @Resource
    private WebPrintMapper webPrintMapper;
    @Resource
    private PrintTokenRedisDAO printTokenRedisDAO;
    @Resource
    private WebPrintConfigMapper webPrintConfigMapper;

    public static final String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改



    @Override
    public Long createWebPrint(WebPrintCreateReqVO createReqVO) {
        // 插入
        WebPrintDO webPrint = WebPrintConvert.INSTANCE.convert(createReqVO);

        webPrintMapper.insert(webPrint);
        // 返回
        return webPrint.getId();
    }

    @Override
    public void updateWebPrint(WebPrintUpdateReqVO updateReqVO) {
        // 校验存在
       validateWebPrintExists(updateReqVO.getId());
        //先删除
        //if(!webPrintDO.getMechineCode().equals(updateReqVO.getMechineCode())){
//            String token = this.getToken();
//            try {
//                String res1 =RequestMethod.getInstance().printerDeletePrinter(token,webPrintDO.getMechineCode());
//                ResponseData responseData1 = JSONUtil.toBean(res1,ResponseData.class);
//                if(responseData1.getError() != 0) {
//                    throw exception(new ErrorCode(999992,
//                            responseData1.getBody() == null ? responseData1.getErrorDescription() : responseData1.getBody()));
//                }
//                String res = RequestMethod.getInstance()
//                        .addPrinter(updateReqVO.getMechineCode(),updateReqVO.getMsign(),token);
//
//                ResponseData responseData = JSONUtil.toBean(res,ResponseData.class);
//                if(responseData.getError() != 0) {
//                    throw exception(new ErrorCode(999992,
//                            responseData.getBody() == null ? responseData.getErrorDescription() : responseData.getBody()));
//                }
//            } catch (Exception e) {
//                throw exception(new ErrorCode(999991,e.getMessage()));
//            }
      //  }
        // 更新
        WebPrintDO updateObj = WebPrintConvert.INSTANCE.convert(updateReqVO);
        webPrintMapper.updateById(updateObj);
    }

    @Override
    public void deleteWebPrint(Long id) {
        // 校验存在
        validateWebPrintExists(id);

        // 删除
        webPrintMapper.deleteById(id);
    }

    private WebPrintDO validateWebPrintExists(Long id) {
        WebPrintDO webPrintDO = webPrintMapper.selectById(id);
        if (webPrintMapper.selectById(id) == null) {
            throw exception(WEB_PRINT_NOT_EXISTS);
        }
        return webPrintDO;
    }

    @Override
    public WebPrintDO getWebPrint(Long id) {
        return webPrintMapper.selectById(id);
    }

    @Override
    public List<WebPrintDO> getWebPrintList() {
        return webPrintMapper.selectList();
    }

    @Override
    public PageResult<WebPrintDO> getWebPrintPage(WebPrintPageReqVO pageReqVO) {
        return webPrintMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WebPrintDO> getWebPrintList(WebPrintExportReqVO exportReqVO) {
        return webPrintMapper.selectList(exportReqVO);
    }

    /**
     * 易联云小票打印
     * @param mechineCode 设备号
     * @param content 打印内容
     * @param originId  订单号
     * @return
     */
    @Override
    public String printOrder(String mechineCode, String content, String originId) {
        WebPrintConfigDO webPrintConfigDO = webPrintConfigMapper.selectOne(new LambdaQueryWrapper<WebPrintConfigDO>()
                .eq(WebPrintConfigDO::getBrand, PrintBrandEnum.YLY.getValue()));
        if(webPrintConfigDO == null){
            throw exception(new ErrorCode(202506050,"易联云打印机未配置"));
        }
        String token = webPrintConfigDO.getAccessToken();
        try {
            RequestMethod.init(webPrintConfigDO.getAppId(),webPrintConfigDO.getAppSecret());
            String json = RequestMethod.getInstance().printIndex(token,mechineCode,content,originId);
            ResponseData responseData = JSONUtil.toBean(json,ResponseData.class);
            if(responseData.getError() != 0) {
                throw exception(new ErrorCode(999992,
                        responseData.getBody() == null ? responseData.getErrorDescription() : responseData.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(new ErrorCode(9999923,e.getMessage()));
        }
        return null;
    }

    @Override
    public void feiePrintOrder(String sn, String content) {
        WebPrintConfigDO webPrintConfigDO = webPrintConfigMapper.selectOne(new LambdaQueryWrapper<WebPrintConfigDO>()
                .eq(WebPrintConfigDO::getBrand, PrintBrandEnum.FEIE.getValue()));
        if(webPrintConfigDO == null){
            throw exception(new ErrorCode(202506070,"飞蛾打印机未配置"));
        }

        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("user",webPrintConfigDO.getAppId());
        String stime = String.valueOf(System.currentTimeMillis()/1000);
        paramMap.put("stime",stime);
        paramMap.put("sig",signature(webPrintConfigDO.getAppId(),webPrintConfigDO.getAppSecret(),stime));
        paramMap.put("sn",sn);
        paramMap.put("apiname","Open_printMsg");
        paramMap.put("content",content);
        paramMap.put("times","1");
        String result2 = HttpRequest.post(URL)
                .formStr(paramMap)//表单内容
                .timeout(30000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(result2);

        if(Integer.valueOf(jsonObject.get("ret").toString()) != 0){
            log.error("error:{}",jsonObject);
            throw exception(new ErrorCode(202506071,jsonObject.get("msg").toString()));
        }
    }

    @Override
    public void savePrintConfig(WebPrintSetVO webPrintSetVO) {
        WebPrintConfigDO webPrintConfigDO = webPrintConfigMapper.selectOne(new LambdaQueryWrapper<WebPrintConfigDO>()
                .eq(WebPrintConfigDO::getBrand,webPrintSetVO.getBrand()));
        if(webPrintConfigDO == null){
            webPrintConfigDO = BeanUtils.toBean(webPrintSetVO,WebPrintConfigDO.class);
            if(PrintBrandEnum.YLY.getValue().equals(webPrintSetVO.getBrand())){
                webPrintConfigDO.setAccessToken(getToken(webPrintSetVO.getAppId(),webPrintSetVO.getAppSecret()));
            }

            webPrintConfigMapper.insert(webPrintConfigDO);
        }else{
            webPrintConfigDO = BeanUtils.toBean(webPrintSetVO,WebPrintConfigDO.class);
            if(PrintBrandEnum.YLY.getValue().equals(webPrintSetVO.getBrand())){
                webPrintConfigDO.setAccessToken(getToken(webPrintSetVO.getAppId(),webPrintSetVO.getAppSecret()));
            }
            webPrintConfigMapper.updateById(webPrintConfigDO);
        }

    }

    @Override
    public WebPrintSetVO getPrintConfig(String brand) {
        WebPrintConfigDO webPrintConfigDO = webPrintConfigMapper.selectOne(new LambdaQueryWrapper<WebPrintConfigDO>()
                .eq(WebPrintConfigDO::getBrand,brand));

        return BeanUtils.toBean(webPrintConfigDO,WebPrintSetVO.class);
    }

    private static String signature(String USER,String UKEY,String STIME){
        String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
        return s;
    }

    /**
     * 获取易联云打印机token
     * @return
     */
    private String getToken(String clientId,String clientSecret){
        RequestMethod.init(clientId,clientSecret);
        try {
            String jsonStr = RequestMethod.getAccessToken();
            ResponseData responseData = JSONUtil.toBean(jsonStr,ResponseData.class);
            if(responseData.getError() != 0) {
                throw exception(new ErrorCode(999992,responseData.getBody() == null ? responseData.getErrorDescription() : responseData.getBody()));
            }
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            TokeData tokeData = jsonObject.get("body", TokeData.class);
            String token = tokeData.getAccessToken();
            return token;
        } catch (Exception e) {
            throw exception(new ErrorCode(99999,e.getMessage()));
        }

    }





}

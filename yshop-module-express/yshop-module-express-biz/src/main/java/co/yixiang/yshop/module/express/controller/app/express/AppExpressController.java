package co.yixiang.yshop.module.express.controller.app.express;

import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.express.dal.redis.express.ExpressRedisDAO;
import co.yixiang.yshop.module.express.kdniao.model.dto.KdhundredApiBaseDTO;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.response.QueryTrackResp;
import com.kuaidi100.sdk.utils.SignUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.module.express.enums.ErrorCodeConstants.EXPRESS_NOT_EXISTS;

@Tag(name = "app - 查询快递")
@RestController
@RequestMapping("/express")
@Validated
public class AppExpressController {

    @Resource
    private ExpressRedisDAO expressRedisDAO;



    @GetMapping("/getLogistic")
    @Parameters({
            @Parameter(name = "shipperCode", description = "快递公司编码", required = true),
            @Parameter(name = "logisticCode", description = "快递单号", required = true),
    })
    @Operation(summary = "查询物流")
    public CommonResult<QueryTrackResp> getLogistic(@RequestParam(value = "shipperCode") String shipperCode,
                                                 @RequestParam("logisticCode") String logisticCode) {
        KdhundredApiBaseDTO kdhundredApiBaseDTO = expressRedisDAO.get();
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(shipperCode);
        queryTrackParam.setNum(logisticCode);
        queryTrackParam.setResultv2("1");
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(kdhundredApiBaseDTO.getCustomer());
        queryTrackReq.setSign(SignUtils.querySign(param, kdhundredApiBaseDTO.getKey(), kdhundredApiBaseDTO.getCustomer()));

        IBaseClient baseClient = new QueryTrack();
        HttpResult result = null;
        try {
            result = baseClient.execute(queryTrackReq);
        } catch (Exception e) {
            throw exception(new ErrorCode(202508240,e.getMessage()));
        }

        QueryTrackResp response = new Gson().fromJson(result.getBody(), QueryTrackResp.class);
        return success(response);
    }



}

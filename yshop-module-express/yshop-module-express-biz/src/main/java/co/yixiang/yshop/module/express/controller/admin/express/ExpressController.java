package co.yixiang.yshop.module.express.controller.admin.express;

import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.express.controller.admin.express.vo.ExpressCreateReqVO;
import co.yixiang.yshop.module.express.controller.admin.express.vo.ExpressPageReqVO;
import co.yixiang.yshop.module.express.controller.admin.express.vo.ExpressRespVO;
import co.yixiang.yshop.module.express.controller.admin.express.vo.ExpressUpdateReqVO;
import co.yixiang.yshop.module.express.convert.express.ExpressConvert;
import co.yixiang.yshop.module.express.dal.dataobject.express.ExpressDO;
import co.yixiang.yshop.module.express.dal.redis.express.ExpressRedisDAO;
import co.yixiang.yshop.module.express.kdniao.model.dto.KdhundredApiBaseDTO;
import co.yixiang.yshop.module.express.service.express.ExpressService;
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
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 快递公司")
@RestController
@RequestMapping("/order/express")
@Validated
public class ExpressController {

    @Resource
    private ExpressService expressService;
    @Resource
    private ExpressRedisDAO expressRedisDAO;

    @PostMapping("/create")
    @Operation(summary = "创建快递公司")
    @PreAuthorize("@ss.hasPermission('order:express:create')")
    public CommonResult<Integer> createExpress(@Valid @RequestBody ExpressCreateReqVO createReqVO) {
        return success(expressService.createExpress(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新快递公司")
    @PreAuthorize("@ss.hasPermission('order:express:update')")
    public CommonResult<Boolean> updateExpress(@Valid @RequestBody ExpressUpdateReqVO updateReqVO) {
        expressService.updateExpress(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除快递公司")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('order:express:delete')")
    public CommonResult<Boolean> deleteExpress(@RequestParam("id") Integer id) {
        expressService.deleteExpress(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得快递公司")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('order:express:query')")
    public CommonResult<ExpressRespVO> getExpress(@RequestParam("id") Integer id) {
        ExpressDO express = expressService.getExpress(id);
        return success(ExpressConvert.INSTANCE.convert(express));
    }

    @GetMapping("/list")
    @Operation(summary = "获得快递公司列表")
    @PreAuthorize("@ss.hasPermission('order:express:query')")
    public CommonResult<List<ExpressRespVO>> getExpressList() {
        List<ExpressDO> list = expressService.getExpressList();
        return success(ExpressConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得快递公司分页")
    @PreAuthorize("@ss.hasPermission('order:express:query')")
    public CommonResult<PageResult<ExpressRespVO>> getExpressPage(@Valid ExpressPageReqVO pageVO) {
        PageResult<ExpressDO> pageResult = expressService.getExpressPage(pageVO);
        return success(ExpressConvert.INSTANCE.convertPage(pageResult));
    }


    @GetMapping("/set")
    @Operation(summary = "获得快递100配置")
    public CommonResult<KdhundredApiBaseDTO> getExpressSet() {
        return success(expressRedisDAO.get());
    }

    @PostMapping("/set")
    @Operation(summary = "快递100配置")
    public CommonResult<Boolean> postExpressSet(@RequestBody KdhundredApiBaseDTO kdhundredApiBaseDTO) {
        expressRedisDAO.set(kdhundredApiBaseDTO);
        return success(true);
    }

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
            throw exception(new ErrorCode(202508241,e.getMessage()));
        }

        QueryTrackResp response = new Gson().fromJson(result.getBody(), QueryTrackResp.class);
        return success(response);
    }



}

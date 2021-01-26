package cn.net.yzl.crm.service.micservice;

import java.util.List;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.logistics.model.vo.ExpressCodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.ApiOperation;

/**
 * 顾客服务接口
 */
@FeignClient(name = "yzl-logistics-server",url = "${api.gateway.url}/logisticsServer/expresscompany")
//@FeignClient(value = "yzl-crm-customer-api")
public interface LogisticsFien {

    @ApiOperation("物流快递追踪轨迹信息")
    @GetMapping("/v1/selectExpressComponyCode")
    ComResponse<List<ExpressCodeVo>> selectExpressComponyCode();

    @ApiOperation("物流快递公司信息")
    @GetMapping("/v1/selectExpressComponyDetail")
    ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail();

}

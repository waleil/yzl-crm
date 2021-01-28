package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.order.ExpressTraceResDTO;
import cn.net.yzl.logistics.model.vo.ExpressTraceVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "orderLogisticsInfoClient",url = "${api.gateway.url}/logisticsServer")
public interface OrderLogisticsInfoClient {



    @ApiOperation(value = "订单物流轨迹")
    @GetMapping("/logistics/findLogisticsTraces")
    public ComResponse<List<ExpressTraceResDTO>> findLogisticsTraces(@RequestParam("companyCode") String companyCode,
                                                                     @RequestParam("mailId")     String mailId);

}

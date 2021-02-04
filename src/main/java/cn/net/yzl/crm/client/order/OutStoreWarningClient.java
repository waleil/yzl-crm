package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderWarningOutStorePageDTO;
import cn.net.yzl.order.model.vo.order.OutStoreWarningDTO;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhouchangsong
 */
@FeignClient(name = "outStoreWarningClient", url = "${api.gateway.url}/orderService/outStoreWarning")
public interface OutStoreWarningClient {

    @GetMapping("v1/getOutStoreWarningDetail")
    @ApiOperation(value = "查询出库预警订单信息")
    ComResponse<OutStoreWarningDTO> getOutStoreWarningDetail();

    @GetMapping("v1/getPageList")
    ComResponse<Page<OrderWarningOutStorePageDTO>> getPageList(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String orderNo);

    @GetMapping("v1/getByBusinessType")
    ComResponse<JSONObject> getByBusinessType(@RequestParam String businessType);

    @PostMapping("v1/saveSysConfig")
    ComResponse<Integer> saveSysConfig(@RequestBody String businessValue);
}

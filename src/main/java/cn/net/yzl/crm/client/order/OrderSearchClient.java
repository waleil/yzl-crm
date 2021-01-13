package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OderListReqDTO;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@FeignClient(name = "orderSerch",url = "${api.gateway.url}/orderService/orderSearch")
public interface OrderSearchClient {

    @ApiOperation(value = "查询订单列表")
    @RequestMapping(path="v1/selectOrderList",method = RequestMethod.POST)
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto);
}

package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderRejectionPageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient(name = "orderRejection", url = "${api.gateway.url}/orderService/orderRejection")
public interface OrderRejectionClient {

    @GetMapping("v1/getOrderRejectionList")
    ComResponse<Page<OrderRejectionPageDTO>> getOrderRejectionList(String orderNo, Integer pageNum, Integer pageSize);
}

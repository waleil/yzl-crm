package cn.net.yzl.crm.client.order;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 订单feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年1月16日,下午12:03:42
 */
//@FeignClient(name = "order", url = "http://localhost:4455/order")
@FeignClient(name = "order", url = "${api.gateway.url}/orderService/order")
public interface OrderFeignClient {
}

package cn.net.yzl.crm.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderRequest;

/**
 * 订单feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年1月16日,下午12:03:42
 */
@FeignClient(name = "order", url = "${api.gateway.url}/orderService/order")
public interface OrderFeignClient {
	/**
	 * 热线工单-购物车-提交订单
	 * 
	 * @param orderRequest 订单信息
	 * @return 创建订单
	 * @author zhangweiwei
	 * @date 2021年1月26日,下午6:01:40
	 */
	@PostMapping("/v1/submitorder")
	ComResponse<Object> submitOrder(@RequestBody OrderRequest orderRequest);

	/**
	 * 按订单编号查询订单信息
	 * 
	 * @param orderNo 订单编号
	 * @return 订单信息
	 * @author zhangweiwei
	 * @date 2021年1月30日,下午2:05:43
	 */
	@GetMapping("/v1/get/{orderNo}")
	ComResponse<OrderM> queryOrder(@PathVariable String orderNo);
}

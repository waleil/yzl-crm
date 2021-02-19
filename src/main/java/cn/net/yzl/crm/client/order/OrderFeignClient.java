package cn.net.yzl.crm.client.order;

import java.math.BigDecimal;

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
@FeignClient(name = "order", url = "${api.gateway.url}/orderService")
public interface OrderFeignClient {
	/**
	 * 热线工单-购物车-提交订单
	 * 
	 * @param orderRequest 订单信息
	 * @return 创建订单
	 * @author zhangweiwei
	 * @date 2021年1月26日,下午6:01:40
	 */
	@PostMapping("/order/v1/submitorder")
	ComResponse<Object> submitOrder(@RequestBody OrderRequest orderRequest);

	/**
	 * 订单列表-编辑
	 * 
	 * @param orderRequest 订单信息
	 * @return 修改订单
	 * @author zhangweiwei
	 * @date 2021年1月31日,上午1:40:14
	 */
	@PostMapping("/order/v1/updateorder")
	ComResponse<Object> updateOrder(@RequestBody OrderRequest orderRequest);

	/**
	 * 订单列表-异常处理-补发订单
	 * 
	 * @param orderRequest 订单信息
	 * @return 补发订单
	 * @author zhangweiwei
	 * @date 2021年2月19日,上午12:11:53
	 */
	@PostMapping("/order/v1/reissueorder")
	ComResponse<Object> reissueOrder(@RequestBody OrderRequest orderRequest);

	/**
	 * 按订单编号查询订单信息
	 * 
	 * @param orderNo 订单编号
	 * @return 订单信息
	 * @author zhangweiwei
	 * @date 2021年1月30日,下午2:05:43
	 */
	@GetMapping("/order/v1/get/{orderNo}")
	ComResponse<OrderM> queryOrder(@PathVariable String orderNo);

	/**
	 * @return 成交金额
	 * @author zhangweiwei
	 * @date 2021年2月18日,下午11:16:40
	 */
	@GetMapping("/order/v1/queryordertotal")
	ComResponse<BigDecimal> queryOrderTotal();
}

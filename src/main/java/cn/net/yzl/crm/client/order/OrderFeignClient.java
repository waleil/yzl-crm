package cn.net.yzl.crm.client.order;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.enums.LeaderBoardType;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.ehr.LeaderBoard;
import cn.net.yzl.order.model.vo.member.MemberChannel;
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
	 * 查询今日/3日/7日业绩排行榜
	 * 
	 * @param boardType     {@link LeaderBoardType}
	 * @param workOrderType 1：热线，2：回访
	 * @return 业绩排行榜列表
	 * @author zhangweiwei
	 * @date 2021年2月23日,上午4:52:42
	 */
	@GetMapping("/order/v1/leaderboard")
	ComResponse<List<LeaderBoard>> queryLeaderboard(@RequestParam LeaderBoardType boardType,
			@RequestParam int workOrderType);

	/**
	 * 查询顾客首单渠道
	 * 
	 * @param memberCard 顾客卡号
	 * @return {@link MemberChannel}
	 * @author zhangweiwei
	 * @date 2021年2月24日,下午10:20:55
	 */
	@GetMapping("/order/v1/memberfirstorderchannel/{memberCard}")
	ComResponse<MemberChannel> queryMemberFirstOrderChannel(@PathVariable String memberCard);

	/**
	 * 订单财务名称集合
	 * 
	 * @param orderNoList 订单号集合
	 * @return
	 * @author zhangweiwei
	 * @date 2021年3月9日,上午3:02:19
	 */
	@PostMapping("/order/v1/financialnames")
	ComResponse<Map<String, OrderM>> queryFinancialNames(@RequestParam List<String> orderNoList);
}

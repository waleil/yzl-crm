package cn.net.yzl.crm.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.CompareOrderIn;
import cn.net.yzl.order.model.vo.order.CompareOrderOut;

/**
 * 对账订单管理feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年2月9日,下午4:39:49
 */
@FeignClient(name = "comparisonmgt", url = "${api.gateway.url}/orderService")
public interface ComparisonMgtFeignClient {
	/**
	 * 查询对账订单列表--支持分页
	 * 
	 * @param orderin 查询条件
	 * @return 对账订单列表
	 * @author zhangweiwei
	 * @date 2021年2月9日,下午4:43:15
	 */
	@PostMapping("/comparisonmgt/v1/querypagelist")
	ComResponse<Page<CompareOrderOut>> queryPageList(@RequestBody CompareOrderIn orderin);
}

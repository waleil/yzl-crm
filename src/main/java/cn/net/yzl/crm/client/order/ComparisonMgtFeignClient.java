package cn.net.yzl.crm.client.order;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 对账订单管理feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年2月9日,下午4:39:49
 */
@FeignClient(name = "comparisonmgt", url = "${api.gateway.url}/orderService")
public interface ComparisonMgtFeignClient {

}

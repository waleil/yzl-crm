package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.client.order.OrderFeignClient;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:42:55
 */
@SpringBootTest
public class OrderFeignClientTests {
	@Resource
	private OrderFeignClient orderFeignClient;

	@Test
	public void testQueryOrder() {
		try {
			System.err.println(this.orderFeignClient.queryOrder("ON1314020T202101301744350095").getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.client.order.OrderSearchClient;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月17日,上午12:00:29
 */
@SpringBootTest
public class OrderSearchClientTests {
	@Resource
	private OrderSearchClient orderSearchClient;

	@Test
	public void testHasRefundByMemberCardNo() {
		try {
			System.err.println(this.orderSearchClient.hasRefundByMemberCardNo("100000003", "2021-01-01 00:00:00",
					"2021-03-01 23:59:59"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

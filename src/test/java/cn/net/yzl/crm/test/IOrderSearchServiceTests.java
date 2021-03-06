package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.service.order.IOrderSearchService;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月6日,下午11:11:04
 */
@SpringBootTest
public class IOrderSearchServiceTests {
	@Resource
	private IOrderSearchService orderSearchService;

	@Test
	public void testSelectLogisticInfo() {
		try {
			String dto = null;
			String companyCode = null;
			String orderNo = "FB12678T210301100230";
			System.err.println(this.orderSearchService.selectLogisticInfo(orderNo, companyCode, dto));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

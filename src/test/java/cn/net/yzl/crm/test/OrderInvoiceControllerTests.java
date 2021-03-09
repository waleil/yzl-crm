package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.activity.model.requestModel.AccountRequest;
import cn.net.yzl.crm.controller.order.OrderInvoiceController;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月2日,下午11:23:12
 */
@SpringBootTest
public class OrderInvoiceControllerTests {
	@Resource
	private OrderInvoiceController controller;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testGetMemberCoupon() {
		try {
			AccountRequest request = new AccountRequest();
			request.setPageNo(1);
			request.setPageSize(10);
			request.setMemberCard("常立雷1");
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.controller.getMemberCoupon(request)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMemberIntegralRecords() {
		try {
			AccountRequest request = new AccountRequest();
			request.setPageNo(1);
			request.setPageSize(10);
			request.setMemberCard("常立雷1");
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.controller.getMemberIntegralRecords(request)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMemberRedBagRecords() {
		try {
			AccountRequest request = new AccountRequest();
			request.setPageNo(1);
			request.setPageSize(10);
			request.setMemberCard("常立雷1");
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.controller.getMemberRedBagRecords(request)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

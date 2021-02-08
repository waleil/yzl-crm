package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.controller.order.AccountDetailController;
import cn.net.yzl.order.model.vo.member.AccountDetailIn;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月8日,上午11:27:01
 */
@SpringBootTest
public class AccountDetailControllerTests {
	@Resource
	private AccountDetailController controller;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testQueryPageList() {
		try {
			AccountDetailIn ad = new AccountDetailIn();
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.controller.queryPageList(ad)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

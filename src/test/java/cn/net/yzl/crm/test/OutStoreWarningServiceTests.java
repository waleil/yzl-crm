package cn.net.yzl.crm.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.order.OutStoreWarningService;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:49:41
 */
@SpringBootTest
public class OutStoreWarningServiceTests {
	@Autowired
	private OutStoreWarningService outStoreWarningService;

	@Test
	public void testSendOutStoreWarningMsg() {
		ComResponse<Boolean> response = outStoreWarningService.sendOutStoreWarningMsg();
		System.out.println("response = " + response);

	}

}

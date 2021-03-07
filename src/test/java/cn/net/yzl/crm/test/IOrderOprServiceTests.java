package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.service.order.IOrderOprService;
import cn.net.yzl.order.model.vo.order.OrderCheckDetailDTO;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月7日,下午2:24:23
 */
@SpringBootTest
public class IOrderOprServiceTests {
	@Resource
	private IOrderOprService orderOprService;

	@Test
	public void testCheckOrder() {
		try {
			OrderCheckDetailDTO dto = new OrderCheckDetailDTO();
			System.err.println(this.orderOprService.checkOrder(dto));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

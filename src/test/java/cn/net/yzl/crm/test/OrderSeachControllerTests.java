package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.controller.order.OrderSeachController;
import cn.net.yzl.order.model.vo.order.OderListReqDTO;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月8日,下午7:43:35
 */
@SpringBootTest
public class OrderSeachControllerTests {
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private OrderSeachController orderSeachController;

	@Test
	public void testSelectOrderList() {
		try {
			OderListReqDTO dto = new OderListReqDTO();
			dto.setPageNo(1);
			dto.setPageSize(10);
			dto.setMemberCardNo("4006926372");
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.orderSeachController.selectOrderList(dto)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

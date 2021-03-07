package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testCheckOrder() {
		try {
			OrderCheckDetailDTO dto = new OrderCheckDetailDTO();
			dto.setCheckDepartId("");
			dto.setCheckStatus(1);
			dto.setCheckUserName("");
			dto.setCheckUserNo("");
			dto.setContractContent("");
			dto.setContractOpinion("");
			dto.setDepartId("");
			dto.setOrderNo("ON202103062200014772826057");
			dto.setRefuseReason("");
			dto.setRemark("");
			dto.setReveiverAddress("甘肃省天水市甘谷县金山镇蒲家山村王瑶西小组");
			dto.setReveiverArea("19267");
			dto.setReveiverAreaName("甘谷县");
			dto.setReveiverCity("44342");
			dto.setReveiverCityName("天水市");
			dto.setReveiverName("王占生");
			dto.setReveiverProvince("24");
			dto.setReveiverProvinceName("甘肃省");
			dto.setReveiverTelphoneNo("0150****4855");

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
//			System.err.println(this.orderOprService.checkOrder(dto));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.net.yzl.crm.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.order.model.vo.order.SettlementDetailDistinctListDTO;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:44:22
 */
@SpringBootTest
public class SettlementFeinTests {
	@Autowired
	private SettlementFein settlementFein;

	@Test
	public void testGetSettlementDetailGroupByOrderNo() {
		ComResponse<List<SettlementDetailDistinctListDTO>> list = settlementFein
				.getSettlementDetailGroupByOrderNo(new ArrayList<>());
		System.out.println(list.getData());
	}

}

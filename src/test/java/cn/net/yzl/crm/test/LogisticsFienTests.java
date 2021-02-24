package cn.net.yzl.crm.test;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.logistics.model.vo.ExpressIndemnity;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:48:52
 */
@SpringBootTest
public class LogisticsFienTests {
	@Resource
	private LogisticsFien logisticsFien;

	@Test
	public void testSettlementLogisticsChargeIndemnity() {
		try {
			ExpressIndemnity indemnity = new ExpressIndemnity();
			indemnity.setCharge(BigDecimal.valueOf(123));
			indemnity.setExpressNum("134657897");
			System.err.println(this.logisticsFien.settlementLogisticsChargeIndemnity(indemnity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

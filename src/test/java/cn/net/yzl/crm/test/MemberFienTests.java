package cn.net.yzl.crm.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.service.micservice.MemberFien;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月18日,下午8:19:03
 */
@SpringBootTest
public class MemberFienTests {
	@Resource
	private MemberFien memberFien;

	@Test
	public void testGetMember() {
		try {
			String member = "10000055";
			System.err.println(this.memberFien.getMember(member));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMemberAmount() {
		try {
			String member = "100000002";
			System.err.println(this.memberFien.getMemberAmount(member).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetReveiverAddress() {
		try {
			String member = "100000002";
			this.memberFien.getReveiverAddress(member).getData().forEach(System.err::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDealOrderCreateUpdateMemberData() {
		try {
			OrderCreateInfoVO vo = new OrderCreateInfoVO();
			vo.setCreateTime(new Date());
			vo.setMemberCard("100000002");
			vo.setOrderNo("ON20210208173415637257287");
			vo.setStaffNo("6666");
			System.err.println(this.memberFien.dealOrderCreateUpdateMemberData(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

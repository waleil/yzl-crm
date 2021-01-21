package cn.net.yzl.crm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月18日,下午7:10:59
 */
public class SimpleTests {
	@Test
	public void testCollect() {
//		OrderDetailIn od1 = new OrderDetailIn();
//		od1.setMealNo("11");
//		od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
//		OrderDetailIn od2 = new OrderDetailIn();
//		od2.setProductCode("22");
//		od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
//		OrderDetailIn od3 = new OrderDetailIn();
//		od3.setProductCode("33");
//		od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
//		OrderDetailIn m1 = new OrderDetailIn();
//		m1.setProductCode("44");
//		m1.setMealFlag(CommonConstant.MEAL_FLAG_0);
//		OrderDetailIn m2 = new OrderDetailIn();
//		m2.setProductCode("55");
//		m2.setMealFlag(CommonConstant.MEAL_FLAG_0);
//		List<OrderDetailIn> meal = Arrays.asList(m1, m2);
//		List<OrderDetailIn> detailIns = Arrays.asList(od1, od2, od3);
//		System.err.println(detailIns.stream().map(OrderDetailIn::getProductCode).collect(Collectors.joining(",")));
//		List<OrderDetailIn> list = detailIns.stream().filter(p -> p.getMealFlag() != CommonConstant.MEAL_FLAG_0)
//				.collect(Collectors.toList());
//		System.err.println(list.size());
	}

	@Test
	public void testOptional() {
		List<MemberPhone> phones = null;
		System.err.println(Optional.ofNullable(phones).filter(p -> !p.isEmpty()).map(m -> m.get(0))
				.map(MemberPhone::getPhone_number).orElse(null));
		phones = new ArrayList<>();
		System.err.println(Optional.ofNullable(phones).filter(p -> !p.isEmpty()).map(m -> m.get(0))
				.map(MemberPhone::getPhone_number).orElse(null));
		MemberPhone mp = new MemberPhone();
		phones.add(mp);
		System.err.println(Optional.ofNullable(phones).filter(p -> !p.isEmpty()).map(m -> m.get(0))
				.map(MemberPhone::getPhone_number).orElse(null));
		mp.setPhone_number("134679313256");
		System.err.println(Optional.ofNullable(phones).filter(p -> !p.isEmpty()).map(m -> m.get(0))
				.map(MemberPhone::getPhone_number).orElse(null));
	}

	@Test
	public void testAtomicInteger() {
		AtomicInteger ai = new AtomicInteger(0);
		Arrays.asList("a", "b", "c").stream().forEach(s -> {
			System.err.println(String.format("%s%s", s, ai.incrementAndGet()));
		});
	}
}

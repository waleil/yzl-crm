package cn.net.yzl.crm;

import java.math.BigDecimal;
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
	public void testJoining() {
		OrderDetailIn od1 = new OrderDetailIn();
		od1.setProductCode("11");
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		List<OrderDetailIn> detailIns = Arrays.asList(od1, od2, od3);
		System.err.println(detailIns.stream().map(OrderDetailIn::getProductCode).collect(Collectors.joining(",")));
	}

	@Test
	public void testCollect() {
		OrderDetailIn od1 = new OrderDetailIn();
		od1.setMealNo("11");
		od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn m1 = new OrderDetailIn();
		m1.setProductCode("44");
		m1.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn m2 = new OrderDetailIn();
		m2.setProductCode("55");
		m2.setMealFlag(CommonConstant.MEAL_FLAG_0);
		List<OrderDetailIn> detailIns = Arrays.asList(od1, od2, od3);
		List<OrderDetailIn> list = detailIns.stream().filter(p -> p.getMealFlag() != CommonConstant.MEAL_FLAG_0)
				.collect(Collectors.toList());
		System.err.println(list.size());
		list.add(new OrderDetailIn());
		System.err.println(list.size());
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

	@Test
	public void testOrder() {
		OrderDetailIn taocan = new OrderDetailIn();
		taocan.setMealPrice(400);// 套餐价
		taocan.setMealName("套餐");

		OrderDetailIn d1 = new OrderDetailIn();
		d1.setProductUnitPrice(100);// 商品单价
		d1.setProductCount(2);// 商品数量
		d1.setTotal(d1.getProductUnitPrice() * d1.getProductCount());// 商品总价=商品单价*商品数量
		d1.setProductCode("AAA");
		d1.setProductName("商品A");

		OrderDetailIn d2 = new OrderDetailIn();
		d2.setProductUnitPrice(50);// 商品单价
		d2.setProductCount(3);// 商品数量
		d2.setTotal(d2.getProductUnitPrice() * d2.getProductCount());
		d2.setProductCode("BBB");
		d2.setProductName("商品B");

		OrderDetailIn d3 = new OrderDetailIn();
		d3.setProductUnitPrice(60);// 商品单价
		d3.setProductCount(3);// 商品数量
		d3.setTotal(d3.getProductUnitPrice() * d3.getProductCount());
		d3.setProductCode("CCC");
		d3.setProductName("商品C");

		List<OrderDetailIn> proList = Arrays.asList(d1, d2, d3);
		int proTotal = proList.stream().mapToInt(OrderDetailIn::getTotal).sum();
		BigDecimal b2 = new BigDecimal(taocan.getMealPrice());
		BigDecimal b3 = new BigDecimal(proTotal);
		proList.stream().forEach(m -> {
			BigDecimal b1 = new BigDecimal(m.getTotal());
			System.err.println(
					String.format("%s: %s", m.getProductName(), b1.multiply(b2).divide(b3, BigDecimal.ROUND_HALF_UP)));
		});
	}
}

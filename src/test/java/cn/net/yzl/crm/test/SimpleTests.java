package cn.net.yzl.crm.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
	public void testBigDecimal() {
		System.err.println(BigDecimal.valueOf(999.99).multiply(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.HALF_UP).toEngineeringString());
		System.err.println(BigDecimal.valueOf(999.99).multiply(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.HALF_UP).toPlainString());
		System.err.println(BigDecimal.valueOf(999.99).multiply(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.HALF_UP).toString());
		System.err.println(Integer.valueOf(BigDecimal.valueOf(999.99).multiply(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.HALF_UP).toString()));
		System.err.println(BigDecimal.valueOf(1137701198).divide(BigDecimal.valueOf(100)));
	}

	@Test
	public void testJoining() {
		OrderDetailIn od1 = new OrderDetailIn();
		od1.setProductCode("11");
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		OrderDetailIn od4 = new OrderDetailIn();
		od4.setProductCode("33");
		List<OrderDetailIn> detailIns = Arrays.asList(od1, od2, od3, od4);
		System.err.println(
				detailIns.stream().map(OrderDetailIn::getProductCode).distinct().collect(Collectors.joining(",")));
	}

	@Test
	public void testGroupingBy() {
		OrderDetailIn od1 = new OrderDetailIn();
		od1.setMealNo("11");
		od1.setMealName("套餐11");
		od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		od2.setProductName("商品22");
		od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		od3.setProductName("商品33");
		od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od4 = new OrderDetailIn();
		od4.setProductCode("44");
		od4.setProductName("商品44");
		od4.setMealFlag(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od5 = new OrderDetailIn();
		od5.setMealNo("55");
		od5.setMealName("套餐55");
		od5.setMealFlag(CommonConstant.MEAL_FLAG_1);
		Map<Integer, List<OrderDetailIn>> odMap = Arrays.asList(od1, od2, od3, od4, od5).stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getMealFlag));
		odMap.entrySet().stream()
				.forEach(en -> System.err.println(String.format("%s\t%s", en.getKey(), en.getValue())));
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
		Arrays.asList("a", "b", "c").stream()
				.forEach(s -> System.err.println(String.format("%s%s", s, ai.incrementAndGet())));
	}

	@Test
	public void testOrderRules() {
		this.testOrderRule1();
		this.testOrderRule2();
		this.testOrderRule3();
	}

	@Test
	public void testOrderRule1() {
		System.err.println("计算规则一：");
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

		List<OrderDetailIn> orderList = Arrays.asList(d1, d2, d3);
		int orderTotal = orderList.stream().mapToInt(OrderDetailIn::getTotal).sum();

		BigDecimal b2 = BigDecimal.valueOf(taocan.getMealPrice());
		BigDecimal b3 = BigDecimal.valueOf(orderTotal);
		orderList.stream().forEach(m -> {
			BigDecimal b1 = BigDecimal.valueOf(m.getTotal());
			BigDecimal b4 = BigDecimal.valueOf(m.getProductCount());
			System.err.println(String.format("%s: %s", m.getProductName(),
					b1.multiply(b2).divide(b3, BigDecimal.ROUND_HALF_UP).divide(b4, BigDecimal.ROUND_HALF_UP)));
		});
	}

	@Test
	public void testOrderRule2() {
		System.err.println("计算规则二：");
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

		List<OrderDetailIn> orderList = Arrays.asList(d1, d2, d3);
		int orderTotal = orderList.stream().mapToInt(OrderDetailIn::getTotal).sum();
		int proTotal = orderList.stream().mapToInt(OrderDetailIn::getProductUnitPrice).sum();
		int cha = orderTotal - taocan.getMealPrice();

		BigDecimal b1 = BigDecimal.valueOf(cha);
		BigDecimal b2 = BigDecimal.valueOf(proTotal);
		orderList.stream().forEach(m -> {
			BigDecimal b3 = BigDecimal.valueOf(m.getProductUnitPrice());
			System.err.println(
					String.format("%s: %s", m.getProductName(), b1.multiply(b3).divide(b2, BigDecimal.ROUND_HALF_UP)));
		});
	}

	@Test
	public void testOrderRule3() {
		System.err.println("计算规则三：");
		OrderDetailIn taocan = new OrderDetailIn();
		taocan.setMealPrice(4686900);// 套餐价
		taocan.setMealName("套餐");

		OrderDetailIn d1 = new OrderDetailIn();
		d1.setProductUnitPrice(321300);// 商品单价
		d1.setProductCount(1);// 商品数量
		d1.setTotal(d1.getProductUnitPrice() * d1.getProductCount());// 商品总价=商品单价*商品数量
		d1.setProductCode("AAA");
		d1.setProductName("商品A");

		OrderDetailIn d2 = new OrderDetailIn();
		d2.setProductUnitPrice(4343400);// 商品单价
		d2.setProductCount(1);// 商品数量
		d2.setTotal(d2.getProductUnitPrice() * d2.getProductCount());
		d2.setProductCode("BBB");
		d2.setProductName("商品B");

		OrderDetailIn d3 = new OrderDetailIn();
		d3.setProductUnitPrice(9900);// 商品单价
		d3.setProductCount(1);// 商品数量
		d3.setTotal(d3.getProductUnitPrice() * d3.getProductCount());
		d3.setProductCode("CCC");
		d3.setProductName("商品C");

		OrderDetailIn d4 = new OrderDetailIn();
		d4.setProductUnitPrice(9900);// 商品单价
		d4.setProductCount(1);// 商品数量
		d4.setTotal(d4.getProductUnitPrice() * d4.getProductCount());
		d4.setProductCode("DDD");
		d4.setProductName("商品D");

		List<OrderDetailIn> orderList = Arrays.asList(d1, d2, d3, d4);
		int orderTotal = orderList.stream().mapToInt(OrderDetailIn::getTotal).sum();
		BigDecimal b1 = BigDecimal.valueOf(taocan.getMealPrice());
		BigDecimal b2 = BigDecimal.valueOf(orderTotal);
		orderList.stream().map(m -> {
			BigDecimal b3 = BigDecimal.valueOf(m.getProductUnitPrice());
			BigDecimal b4 = b1.multiply(b3).divide(b2, 2, BigDecimal.ROUND_HALF_UP);
//			System.err.println(String.format("%s: %s", m.getProductName(), b4.intValue()));
			m.setProductUnitPrice(b4.intValue());
			return m;
		}).collect(Collectors.toList()).forEach(
				od -> System.err.println(String.format("%s: %s", od.getProductName(), od.getProductUnitPrice())));
	}
}

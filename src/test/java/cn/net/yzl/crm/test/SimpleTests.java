package cn.net.yzl.crm.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.lang.Tuple;
import cn.net.yzl.activity.model.dto.CalculateProductDto;
import cn.net.yzl.activity.model.enums.ActivityTypeEnum;
import cn.net.yzl.activity.model.enums.DiscountTypeEnum;
import cn.net.yzl.activity.model.enums.UseDiscountTypeEnum;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月18日,下午7:10:59
 */
@SuppressWarnings("rawtypes")
public class SimpleTests {
	@Test
	public void testCompletableFuture() {
		long begin = System.currentTimeMillis();
		CompletableFuture[] allof = { CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			return 1;
		}), CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			return 2;
		}) };
		CompletableFuture.allOf(allof);
		int sum = Arrays.stream(allof).mapToInt(m -> {
			try {
				return (Integer) (m.get(1, TimeUnit.SECONDS));
			} catch (Exception e) {
//				e.printStackTrace();
				return -1;
			}
		}).sum();
		long end = System.currentTimeMillis() - begin;
		System.err.println(String.format("求和：%s, 耗时：%s秒", sum, end / 1000));
	}

	@Test
	public void testHtmlUtils() {
		System.err.println(HtmlUtils.htmlEscape("<p>我是一个段落标记</p><script>alert(123)</script><div>我是一个div</div>"));
		System.err.println(HtmlUtils.htmlUnescape(
				"&lt;p&gt;我是一个段落标记&lt;/p&gt;&lt;script&gt;alert(123)&lt;/script&gt;&lt;div&gt;我是一个div&lt;/div&gt;"));
	}

	@Test
	public void testRegex() {
		try {
			Pattern pattern = Pattern.compile("^\\d+$");
			System.err.println(pattern.matcher("131479").matches());
			System.err.println(pattern.matcher("131我是谁479").matches());
			System.err.println(pattern.matcher("-131.479").matches());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testValues() {
		int code = 1;
		System.err.println(Arrays.stream(UseDiscountTypeEnum.values())
				.filter(p -> Integer.compare(p.getCode(), code) == 0).findFirst().orElse(null));
		System.err.println(Arrays.stream(DiscountTypeEnum.values()).filter(p -> Integer.compare(p.getCode(), code) == 0)
				.findFirst().orElse(null));
		System.err.println(Arrays.stream(ActivityTypeEnum.values()).filter(p -> Integer.compare(p.getCode(), code) == 0)
				.findFirst().orElse(null));
	}

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

		System.err.println(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(0L)) > 0);
		System.err.println(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(0D)) > 0);
		System.err.println(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(0)) > 0);
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
		od1.setProductCode("11");
		od1.setProductName("套餐11");
		od1.setProductType(CommonConstant.MEAL_FLAG_1);
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		od2.setProductName("商品22");
		od2.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		od3.setProductName("商品33");
		od3.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od4 = new OrderDetailIn();
		od4.setProductCode("44");
		od4.setProductName("商品44");
		od4.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od5 = new OrderDetailIn();
		od5.setProductCode("55");
		od5.setProductName("套餐55");
		od5.setProductType(CommonConstant.MEAL_FLAG_1);
		Map<Integer, List<OrderDetailIn>> odMap = Arrays.asList(od1, od2, od3, od4, od5).stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getProductType));
		odMap.entrySet().stream()
				.forEach(en -> System.err.println(String.format("%s\t%s", en.getKey(), en.getValue())));
	}

	@Test
	public void testCollect() {
		OrderDetailIn od1 = new OrderDetailIn();
		od1.setProductCode("11");
		od1.setProductType(CommonConstant.MEAL_FLAG_1);
		OrderDetailIn od2 = new OrderDetailIn();
		od2.setProductCode("22");
		od2.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn od3 = new OrderDetailIn();
		od3.setProductCode("33");
		od3.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn m1 = new OrderDetailIn();
		m1.setProductCode("44");
		m1.setProductType(CommonConstant.MEAL_FLAG_0);
		OrderDetailIn m2 = new OrderDetailIn();
		m2.setProductCode("55");
		m2.setProductType(CommonConstant.MEAL_FLAG_0);
		List<OrderDetailIn> detailIns = Arrays.asList(od1, od2, od3);
		List<OrderDetailIn> list = detailIns.stream().filter(p -> p.getProductType() != CommonConstant.MEAL_FLAG_0)
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
		taocan.setProductUnitPrice(400D);// 套餐价
		taocan.setProductName("套餐");

		OrderDetailIn d1 = new OrderDetailIn();
		d1.setProductUnitPrice(100D);// 商品单价
		d1.setProductCount(2);// 商品数量
		d1.setTotal(d1.getProductUnitPrice() * d1.getProductCount());// 商品总价=商品单价*商品数量
		d1.setProductCode("AAA");
		d1.setProductName("商品A");

		OrderDetailIn d2 = new OrderDetailIn();
		d2.setProductUnitPrice(50D);// 商品单价
		d2.setProductCount(3);// 商品数量
		d2.setTotal(d2.getProductUnitPrice() * d2.getProductCount());
		d2.setProductCode("BBB");
		d2.setProductName("商品B");

		OrderDetailIn d3 = new OrderDetailIn();
		d3.setProductUnitPrice(60D);// 商品单价
		d3.setProductCount(3);// 商品数量
		d3.setTotal(d3.getProductUnitPrice() * d3.getProductCount());
		d3.setProductCode("CCC");
		d3.setProductName("商品C");

		List<OrderDetailIn> orderList = Arrays.asList(d1, d2, d3);
		double orderTotal = orderList.stream().mapToDouble(OrderDetailIn::getTotal).sum();

		BigDecimal b2 = BigDecimal.valueOf(taocan.getProductUnitPrice());
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
		taocan.setProductUnitPrice(400D);// 套餐价
		taocan.setProductName("套餐");

		OrderDetailIn d1 = new OrderDetailIn();
		d1.setProductUnitPrice(100D);// 商品单价
		d1.setProductCount(2);// 商品数量
		d1.setTotal(d1.getProductUnitPrice() * d1.getProductCount());// 商品总价=商品单价*商品数量
		d1.setProductCode("AAA");
		d1.setProductName("商品A");

		OrderDetailIn d2 = new OrderDetailIn();
		d2.setProductUnitPrice(50D);// 商品单价
		d2.setProductCount(3);// 商品数量
		d2.setTotal(d2.getProductUnitPrice() * d2.getProductCount());
		d2.setProductCode("BBB");
		d2.setProductName("商品B");

		OrderDetailIn d3 = new OrderDetailIn();
		d3.setProductUnitPrice(60D);// 商品单价
		d3.setProductCount(3);// 商品数量
		d3.setTotal(d3.getProductUnitPrice() * d3.getProductCount());
		d3.setProductCode("CCC");
		d3.setProductName("商品C");

		List<OrderDetailIn> orderList = Arrays.asList(d1, d2, d3);
		double orderTotal = orderList.stream().mapToDouble(OrderDetailIn::getTotal).sum();
		double proTotal = orderList.stream().mapToDouble(OrderDetailIn::getProductUnitPrice).sum();
		double cha = orderTotal - taocan.getProductUnitPrice();

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
		OrderDetail taocan = new OrderDetail();
		taocan.setMealPrice(200 * 100);// 套餐价
		taocan.setMealCount(1);// 套餐数量
		taocan.setMealNo("T0000162");
		taocan.setMealName("工单测试套餐");

		OrderDetail d1 = new OrderDetail();
		d1.setProductUnitPrice(3689);// 商品单价
		d1.setProductCount(2 * taocan.getMealCount());// 商品数量
		d1.setTotal(d1.getProductUnitPrice() * d1.getProductCount());// 商品总价=商品单价*商品数量
		d1.setProductCode("10000016");
		d1.setProductName("王帅测库存专用商品");

		OrderDetail d2 = new OrderDetail();
		d2.setProductUnitPrice(10000);// 商品单价
		d2.setProductCount(2 * taocan.getMealCount());// 商品数量
		d2.setTotal(d2.getProductUnitPrice() * d2.getProductCount());
		d2.setProductCode("10000003");
		d2.setProductName("测试订单专用商品");

		OrderDetail d3 = new OrderDetail();
		d3.setProductUnitPrice(10000);// 商品单价
		d3.setProductCount(1 * taocan.getMealCount());// 商品数量
		d3.setTotal(d3.getProductUnitPrice() * d3.getProductCount());
		d3.setProductCode("10000003");
		d3.setProductName("测试订单专用商品");

		List<OrderDetail> orderList = Arrays.asList(d1, d2);
		// 套餐价
		BigDecimal mealPrice = BigDecimal.valueOf(taocan.getMealPrice() * taocan.getMealCount());
		System.err.println("套餐价：" + mealPrice);
		// 原始商品订单总价
		BigDecimal orderTotal = BigDecimal.valueOf(orderList.stream().mapToInt(OrderDetail::getTotal).sum());
		System.err.println("原始商品订单总价：" + orderTotal);
		orderList.stream().map(m -> {
			BigDecimal productUnitPrice = BigDecimal.valueOf(m.getProductUnitPrice());
			BigDecimal b4 = mealPrice.multiply(productUnitPrice).divide(orderTotal, 0, BigDecimal.ROUND_HALF_UP);
			m.setProductUnitPrice(b4.intValue());
			m.setTotal(m.getProductUnitPrice() * m.getProductCount());
			return m;
		}).collect(Collectors.toList()).forEach(od -> System.err.println(String.format("%s: %s*%s=%s",
				od.getProductName(), od.getProductUnitPrice(), od.getProductCount(), od.getTotal())));
	}

	@Test
	public void testDate() {
		System.err.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
		QueryIds.userNo.set("zhangweiwei");
		System.err.println(Optional.ofNullable(QueryIds.userNo.get()).filter(p -> !p.isEmpty()).orElse("14020"));
	}

	@Test
	public void testMerge() {
		Tuple p1 = new Tuple("aa", 5);
		Tuple p2 = new Tuple("bb", 4);
		Tuple p3 = new Tuple("cc", 3);
		Tuple p11 = new Tuple("aa", 5);
		Tuple p21 = new Tuple("bb", 4);
		Tuple p31 = new Tuple("cc", 3);
		Map<String, Integer> map = new HashMap<>();
		Arrays.asList(p1, p2, p3, p11, p21, p31).stream().forEach(p -> map.merge(p.get(0), p.get(1), Integer::sum));
		System.err.println(map);
	}

	@Test
	public void testPage() {
		System.err.println(AssemblerResultUtil.resultAssembler(Arrays.asList(1, 2, 3)));
	}

	@Test
	public void testCheckOrderAmount() {
		try {
			OrderIn order = new OrderIn();
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductUnitPrice(100D);
			order.getOrderDetailIns().add(od1);

			CheckOrderAmountRequest request = new CheckOrderAmountRequest();
			request.setAdvertBusNo(order.getAdvertBusNo());
			request.setMemberCard(order.getMemberCardNo());
			request.setProductTotal(order.getProductTotal().longValue());
			order.getOrderDetailIns().stream().map(m -> {
				CalculateProductDto dto = new CalculateProductDto();
				dto.setActivityBusNo(m.getActivityBusNo());
				dto.setActivityProductBusNo(m.getActivityProductBusNo());
				dto.setActivityType(m.getActivityType());
				dto.setCouponDiscountId(m.getCouponDiscountId());
				dto.setDiscountId(m.getDiscountId());
				dto.setDiscountType(m.getDiscountType());
				dto.setLimitDownPrice(m.getLimitDownPrice());
				dto.setMemberCouponId(m.getMemberCouponId());
				dto.setProductCode(m.getProductCode());
				dto.setProductCount(m.getProductCount());
				dto.setSalePrice(
						BigDecimal.valueOf(m.getProductUnitPrice()).multiply(BigDecimal.valueOf(100)).longValue());
				dto.setUseDiscountType(m.getUseDiscountType());
				return dto;
			}).collect(Collectors.toList());

			System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(order));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNumber() {
		Object a = new Long("1");
		Object b = new Integer("2");
		System.err.println(((Number) a).longValue());
		System.err.println(((Number) b).longValue());
	}

	@Test
	public void testDuration() {
		Duration between = Duration.between(
				LocalDateTime.parse("2021-03-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse("2021-03-30 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.err.println(between.toDays());
	}
}

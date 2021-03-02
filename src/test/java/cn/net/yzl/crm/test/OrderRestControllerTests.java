package cn.net.yzl.crm.test;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.controller.order.OrderRestController;
import cn.net.yzl.crm.model.order.CalcOrderIn;
import cn.net.yzl.crm.model.order.CalcOrderIn.CalculateOrderProductDto;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;
import cn.net.yzl.order.model.vo.order.ReissueOrderIn;
import cn.net.yzl.order.model.vo.order.UpdateOrderIn;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月23日,上午11:12:13
 */
@SpringBootTest
public class OrderRestControllerTests {
	@Resource
	private OrderRestController orderRestController;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testSubmitOrder() {
		try {
			OrderIn order = new OrderIn();
			order.setMediaChannel(0);
			order.setMediaName(null);
			order.setMediaType(0);
			order.setMemberName(null);
			order.setMemberTelphoneNo(null);
			order.setWorkBatchNo("3538");
			order.setWorkOrderNo(13226);
			order.setWorkOrderType(2);
			order.setAdvertBusNo(1001L);// 广告业务主键
			order.setMemberCardNo("10000055");
			order.setMediaNo(1001);
			order.setPayType(CommonConstant.PAY_TYPE_1);
			order.setRemark("赠品包含套餐");
			order.setReveiverAddressNo(5302);
			order.setProductTotal(BigDecimal.valueOf(998));// 商品总额 单位分
			order.setAmountStored(BigDecimal.valueOf(0));

			OrderDetailIn od1 = new OrderDetailIn();
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			od1.setMealFlag(CommonConstant.GIFT_FLAG_0);
			od1.setProductCode("10000172");
			od1.setProductCount(1);
			od1.setProductType(CommonConstant.MEAL_FLAG_0);
			od1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			OrderDetailIn od2 = new OrderDetailIn();
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_1);
			od2.setMealFlag(CommonConstant.GIFT_FLAG_1);
			od2.setProductCode("T0000163");
			od2.setProductCount(1);
			od2.setProductType(CommonConstant.MEAL_FLAG_1);
			od2.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			order.getOrderDetailIns().add(od1);
			order.getOrderDetailIns().add(od2);

			QueryIds.userNo.set("14020");

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
			System.err.println(JSON.toJSONString(this.orderRestController.submitOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSubmitOrderForProduct() {
		try {
			OrderIn order = new OrderIn();
			order.setProductTotal(BigDecimal.valueOf(940));// 商品总额 单位分
			order.setAdvertBusNo(555L);// 广告业务主键
			order.setMemberCardNo("100000002");
			order.setReveiverAddressNo(482416);
			order.setMediaChannel(0);
			order.setMemberTelphoneNo("12345678901");
			order.setPayType(CommonConstant.PAY_TYPE_0);
//			order.setAmountStored(BigDecimal.valueOf(3531));
			order.setMediaType(1);
			order.setMediaNo(1001);
			order.setMediaName("陕西卫视");

			OrderDetailIn od1 = new OrderDetailIn();
//			od1.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			od1.setActivityProductBusNo(20L);// 活动商品业务主键
//			od1.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			od1.setDiscountType(1);// 优惠方式：0满减，1折扣，2红包
//			od1.setDiscountId(7);// 使用的优惠主键
//			od1.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
//			od1.setProductUnitPrice(200D);// 商品销售价 单位分
			od1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od1.setProductCode("10000156");
			od1.setProductType(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			OrderDetailIn od2 = new OrderDetailIn();
//			od2.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			od2.setActivityProductBusNo(20L);// 活动商品业务主键
//			od2.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			od2.setDiscountType(1);// 优惠方式：0满减，1折扣，2红包
//			od2.setDiscountId(8);// 使用的优惠主键
//			od2.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
//			od2.setProductUnitPrice(200D);// 商品销售价 单位分
			od2.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od2.setProductCode("10000155");
			od2.setProductType(CommonConstant.MEAL_FLAG_0);
			od2.setProductCount(1);
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			OrderDetailIn od3 = new OrderDetailIn();
//			od3.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			od3.setActivityProductBusNo(20L);// 活动商品业务主键
//			od3.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			od3.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
//			od3.setDiscountId(7);// 使用的优惠主键
//			od3.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
//			od3.setProductUnitPrice(200D);// 商品销售价 单位分
			od3.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od3.setProductCode("10000152");
			od3.setProductType(CommonConstant.MEAL_FLAG_0);
			od3.setProductCount(1);
			od3.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			order.getOrderDetailIns().add(od1);
			order.getOrderDetailIns().add(od2);
			order.getOrderDetailIns().add(od3);

			QueryIds.userNo.set("14020");

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
			System.err.println(JSON.toJSONString(this.orderRestController.submitOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSubmitOrderForMeal() {
		try {
			OrderIn order = new OrderIn();
			order.setMediaChannel(0);
			order.setMediaType(0);
			order.setWorkBatchNo("3960");
			order.setWorkOrderNo(13259);
			order.setWorkOrderType(2);
			order.setMemberCardNo("9000000171");
//			order.setAdvertBusNo(555L);
			order.setPayType(CommonConstant.PAY_TYPE_1);
			order.setReveiverAddressNo(3604474);
//			order.setMemberTelphoneNo("12345678901");
			order.setProductTotal(BigDecimal.valueOf(200));
			order.setAmountStored(BigDecimal.ZERO);

			OrderDetailIn od1 = new OrderDetailIn();
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
			od1.setProductCode("T0000162");
			od1.setProductCount(1);
			od1.setProductType(CommonConstant.MEAL_FLAG_1);
//			od1.setProductUnitPrice(1500D);
			od1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);
//			od1.setLimitDownPrice(0L);

			order.getOrderDetailIns().add(od1);

			QueryIds.userNo.set("14020");

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
			System.err.println(JSON.toJSONString(this.orderRestController.submitOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateOrderForProduct() {
		try {
			UpdateOrderIn order = new UpdateOrderIn();
			order.setOrderNo("ON1314020T202102020009215927");

			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("10000156");
			od1.setProductType(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(3);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			OrderDetailIn od2 = new OrderDetailIn();
			od2.setProductCode("10000155");
			od2.setProductType(CommonConstant.MEAL_FLAG_0);
			od2.setProductCount(3);
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			OrderDetailIn od3 = new OrderDetailIn();
			od3.setProductCode("10000152");
			od3.setProductType(CommonConstant.MEAL_FLAG_0);
			od3.setProductCount(3);
			od3.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			order.getOrderDetailIns().add(od1);
			order.getOrderDetailIns().add(od2);
			order.getOrderDetailIns().add(od3);

			QueryIds.userNo.set("14020");

			System.err.println(this.orderRestController.updateOrder(order));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateOrderForMeal() {
		try {
			UpdateOrderIn order = new UpdateOrderIn();
			order.setOrderNo("ON1314020T202102020019455929");

			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("T0000155");
			od1.setProductCount(1);
			od1.setProductType(CommonConstant.MEAL_FLAG_1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			order.getOrderDetailIns().add(od1);

			QueryIds.userNo.set("14020");

			System.err.println(JSON.toJSONString(this.orderRestController.updateOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCalcOrderForProduct() {
		try {
			CalcOrderIn order = new CalcOrderIn();
			order.setMemberCard("100000002");// 会员卡号
			order.setAdvertBusNo(555L);// 广告业务主键

			CalculateOrderProductDto a1 = new CalculateOrderProductDto();
//			a1.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			a1.setActivityProductBusNo(20L);// 活动商品业务主键
//			a1.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			a1.setCouponDiscountId(null);// 使用的优惠券折扣ID
//			a1.setDiscountId(7);// 使用的优惠主键
//			a1.setDiscountType(1);// 优惠方式：0满减，1折扣，2红包
//			a1.setMemberCouponId(null);// 使用的优惠券ID
			a1.setProductCode("10000156");// 商品code
			a1.setProductCount(1);// 商品数量
			a1.setProductType(CommonConstant.MEAL_FLAG_0);// 商品类型
			a1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			a1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			CalculateOrderProductDto a2 = new CalculateOrderProductDto();
//			a2.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			a2.setActivityProductBusNo(20L);// 活动商品业务主键
//			a2.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			a2.setCouponDiscountId(null);// 使用的优惠券折扣ID
//			a2.setDiscountId(7);// 使用的优惠主键
//			a2.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
//			a2.setMemberCouponId(null);// 使用的优惠券ID
			a2.setProductCode("10000155");// 商品code
			a2.setProductCount(1);// 商品数量
			a2.setProductType(CommonConstant.MEAL_FLAG_0);// 商品类型
			a2.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			a2.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			CalculateOrderProductDto a3 = new CalculateOrderProductDto();
//			a3.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
//			a3.setActivityProductBusNo(20L);// 活动商品业务主键
//			a3.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
//			a3.setCouponDiscountId(null);// 使用的优惠券折扣ID
//			a3.setDiscountId(7);// 使用的优惠主键
//			a3.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
//			a3.setMemberCouponId(null);// 使用的优惠券ID
			a3.setProductCode("10000152");// 商品code
			a3.setProductCount(1);// 商品数量
			a3.setProductType(CommonConstant.MEAL_FLAG_0);// 商品类型
			a3.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			a3.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			order.getCalculateProductDtos().add(a1);
			order.getCalculateProductDtos().add(a2);
			order.getCalculateProductDtos().add(a3);

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
			System.err.println(JSON.toJSONString(this.orderRestController.calcOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCalcOrderForMeal() {
		try {
			CalcOrderIn order = new CalcOrderIn();
			order.setAdvertBusNo(555L);
			order.setMemberCard("100000002");

			CalculateOrderProductDto a1 = new CalculateOrderProductDto();
			a1.setActivityBusNo(null);// 活动业务/会员优惠业务主键
			a1.setActivityProductBusNo(null);// 活动商品业务主键
			a1.setActivityType(null);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			a1.setCouponDiscountId(null);// 使用的优惠券折扣ID
			a1.setDiscountId(null);// 使用的优惠主键
			a1.setDiscountType(null);// 优惠方式：0满减，1折扣，2红包
			a1.setMemberCouponId(null);// 使用的优惠券ID
			a1.setProductCode("T0000155");// 商品code
			a1.setProductCount(1);// 商品数量
			a1.setProductType(CommonConstant.MEAL_FLAG_1);// 商品类型
			a1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			a1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			order.getCalculateProductDtos().add(a1);

			System.err.println(JSON.toJSONString(this.orderRestController.calcOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReissueOrder() {
		try {
			ReissueOrderIn order = new ReissueOrderIn();
			order.setOrderNo("ON202103021051196902825492");
			order.setPayAmount(BigDecimal.valueOf(1000));
			order.setRemark("赔了");

			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("10000003");
			od1.setProductType(CommonConstant.MEAL_FLAG_0);
			od1.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);

			order.getOrderDetailIns().add(od1);

			QueryIds.userNo.set("14020");

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
			System.err.println(JSON.toJSONString(this.orderRestController.reissueOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

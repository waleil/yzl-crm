package cn.net.yzl.crm.test;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;

import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.controller.order.OrderRestController;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;
import cn.net.yzl.order.model.vo.order.UpdateOrderIn;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月23日,上午11:12:13
 */
@SpringBootTest
public class OrderRestControllerTests {
	@Resource
	private MealClient mealClient;
	@Resource
	private ProductClient productClient;
	@Resource
	private MemberFien memberFien;
	@Resource
	private EhrStaffClient ehrStaffClient;
	@Resource
	private OrderRestController orderRestController;
	@Resource
	private OrderFeignClient orderFeignClient;
	@Value("${api.gateway.url}")
	private String apiGateWayUrl;

	@Test
	public void testQueryListProductMealByCodes() {
		try {
			String codes = "T0000145,T0000142";
			this.mealClient.queryByIds(codes).getData().forEach(System.err::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryByCodes() {
		try {
			System.err.println(String.format("%s%s%s", this.apiGateWayUrl, ProductClient.SUFFIX_URL,
					ProductClient.INCREASE_STOCK_URL));
			String codes = "10000130,10000114,10000106";
			this.productClient.queryByProductCodes(codes).getData().forEach(System.err::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMember() {
		try {
			System.err.println(String.format("%s%s%s", this.apiGateWayUrl, MemberFien.SUFFIX_URL,
					MemberFien.CUSTOMER_AMOUNT_OPERATION_URL));
			String member = "100000002";
			System.err.println(this.memberFien.getMember(member).getData());
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
	public void testGetMemberAmount() {
		try {
			String member = "100000002";
			System.err.println(this.memberFien.getMemberAmount(member).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDetailsByNo() {
		try {
			String staffno = "6666";
			System.err.println(this.ehrStaffClient.getDetailsByNo(staffno).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDepartById() {
		try {
			Integer departid = 1;
			System.err.println(this.ehrStaffClient.getDepartById(departid).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSubmitOrderForProduct() {
		try {
			OrderIn order = new OrderIn();
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("10000156");
			od1.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(2);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_1);
			OrderDetailIn od2 = new OrderDetailIn();
			od2.setProductCode("10000155");
			od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od2.setProductCount(2);
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			OrderDetailIn od3 = new OrderDetailIn();
			od3.setProductCode("10000152");
			od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od3.setProductCount(2);
			od3.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			order.getOrderDetailIns().add(od1);
			order.getOrderDetailIns().add(od2);
			order.getOrderDetailIns().add(od3);
			order.setMemberCardNo("100000002");
			order.setReveiverAddressNo(482416);
			order.setMediaChannel(0);
			order.setMemberTelphoneNo("12345678901");
			order.setPayType(CommonConstant.PAY_TYPE_0);
			QueryIds.userNo.set("14020");
			System.err.println(JSON.toJSONString(this.orderRestController.submitOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSubmitOrderForMeal() {
		try {
			OrderIn order = new OrderIn();
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setMealNo("T0000155");
			od1.setMealCount(2);
			od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			order.getOrderDetailIns().add(od1);
			order.setMemberCardNo("100000002");
			order.setReveiverAddressNo(482416);
			order.setMediaChannel(0);
			order.setMemberTelphoneNo("12345678901");
			order.setPayType(CommonConstant.PAY_TYPE_0);
			QueryIds.userNo.set("14020");
			System.err.println(JSON.toJSONString(this.orderRestController.submitOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProductReduce() {
		try {
			OrderProductVO vo = new OrderProductVO();
			vo.setOrderNo("ON1314020T202101271816500065");
			ProductReduceVO p1 = new ProductReduceVO();
			p1.setNum(1);
			p1.setOrderNo(vo.getOrderNo());
			p1.setProductCode("10000145");
			ProductReduceVO p2 = new ProductReduceVO();
			p2.setNum(1);
			p2.setOrderNo(vo.getOrderNo());
			p2.setProductCode("10000144");
			ProductReduceVO p4 = new ProductReduceVO();
			p4.setNum(1);
			p4.setOrderNo(vo.getOrderNo());
			p4.setProductCode("10000139");
			vo.setProductReduceVOS(Arrays.asList(p1, p2, p4));
			System.err.println(this.productClient.productReduce(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryOrder() {
		try {
			System.err.println(this.orderFeignClient.queryOrder("ON1314020T202101301744350095").getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateOrderForProduct() {
		try {
			UpdateOrderIn order=new UpdateOrderIn();
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("10000156");
			od1.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(3);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			OrderDetailIn od2 = new OrderDetailIn();
			od2.setProductCode("10000155");
			od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od2.setProductCount(3);
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			OrderDetailIn od3 = new OrderDetailIn();
			od3.setProductCode("10000152");
			od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od3.setProductCount(3);
			od3.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			order.getOrderDetailIns().add(od1);
			order.getOrderDetailIns().add(od2);
			order.getOrderDetailIns().add(od3);
			order.setOrderNo("ON1314020T202101311314180097");
			QueryIds.userNo.set("14020");
			System.err.println(this.orderRestController.updateOrder(order));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

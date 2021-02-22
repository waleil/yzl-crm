package cn.net.yzl.crm.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.activity.model.dto.CalculateProductDto;
import cn.net.yzl.activity.model.dto.OrderSubmitProductDto;
import cn.net.yzl.activity.model.enums.ActivityTypeEnum;
import cn.net.yzl.activity.model.enums.DiscountTypeEnum;
import cn.net.yzl.activity.model.enums.UseDiscountTypeEnum;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.OrderSubmitRequest;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.controller.order.OrderRestController;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.order.CalcOrderIn;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.OutStoreWarningService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;
import cn.net.yzl.order.model.vo.order.SettlementDetailDistinctListDTO;
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
	@Autowired
	private SettlementFein settlementFein;
	@Autowired
	private OutStoreWarningService outStoreWarningService;
	@Resource
	private ActivityClient activityClient;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void sendOutStoreWarningMsg() {
		ComResponse<Boolean> response = outStoreWarningService.sendOutStoreWarningMsg();
		System.out.println("response = " + response);

	}

	@Test
	public void testSettlementFein() {
		ComResponse<List<SettlementDetailDistinctListDTO>> list = settlementFein
				.getSettlementDetailGroupByOrderNo(new ArrayList<>());
		System.out.println(list.getData());
	}

	@Test
	public void getDetailsByNo() {
		ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo("11803");
		if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
		}
		System.out.println(userNo.getData());
	}

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
			String codes = "10000156,10000155,10000152";
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
			String staffno = "14020";
			System.err.println(this.ehrStaffClient.getDetailsByNo(staffno));
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
			order.setProductTotal(39990L);// 商品总额 单位分
			order.setAdvertBusNo(555L);// 广告业务主键
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			od1.setActivityProductBusNo(20L);// 活动商品业务主键
			od1.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			od1.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			od1.setDiscountId(7);// 使用的优惠主键
			od1.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
			od1.setProductUnitPrice(200D);// 商品销售价 单位分
			od1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od1.setProductCode("10000156");
			od1.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od1.setProductCount(2);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			OrderDetailIn od2 = new OrderDetailIn();
			od2.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			od2.setActivityProductBusNo(20L);// 活动商品业务主键
			od2.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			od2.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			od2.setDiscountId(7);// 使用的优惠主键
			od2.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
			od2.setProductUnitPrice(200D);// 商品销售价 单位分
			od2.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od2.setProductCode("10000155");
			od2.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od2.setProductCount(2);
			od2.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			OrderDetailIn od3 = new OrderDetailIn();
			od3.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			od3.setActivityProductBusNo(20L);// 活动商品业务主键
			od3.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			od3.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			od3.setDiscountId(7);// 使用的优惠主键
			od3.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
			od3.setProductUnitPrice(200D);// 商品销售价 单位分
			od3.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			od3.setProductCode("10000152");
			od3.setMealFlag(CommonConstant.MEAL_FLAG_0);
			od3.setProductCount(2);
			od3.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			order.getOrderDetailIns().add(od1);
//			order.getOrderDetailIns().add(od2);
//			order.getOrderDetailIns().add(od3);
			order.setMemberCardNo("100000002");
			order.setReveiverAddressNo(482416);
			order.setMediaChannel(0);
			order.setMemberTelphoneNo("12345678901");
			order.setPayType(CommonConstant.PAY_TYPE_0);
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
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("T0000155");
			od1.setProductCount(2);
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
			UpdateOrderIn order = new UpdateOrderIn();
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
			order.setOrderNo("ON1314020T202102020009215927");
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
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("T0000155");
			od1.setProductCount(1);
			od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
			order.getOrderDetailIns().add(od1);
			order.setOrderNo("ON1314020T202102020019455929");
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
			CalculateProductDto a1 = new CalculateProductDto();
			a1.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			a1.setActivityProductBusNo(20L);// 活动商品业务主键
			a1.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			a1.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			a1.setDiscountId(7);// 使用的优惠主键
			a1.setProductCode("10000156");// 商品code
			a1.setProductCount(2);// 商品数量
			a1.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
			a1.setSalePrice(20000L);// 商品销售价 单位分
			a1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			CalculateProductDto a2 = new CalculateProductDto();
			a2.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			a2.setActivityProductBusNo(20L);// 活动商品业务主键
			a2.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			a2.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			a2.setDiscountId(7);// 使用的优惠主键
			a2.setProductCode("10000155");// 商品code
			a2.setProductCount(2);// 商品数量
			a2.setLimitDownPrice(20000L);// 商品最低折扣价 单位分
			a2.setSalePrice(30000L);// 商品销售价 单位分
			a2.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			order.getCalculateProductDtos().add(a1);
			order.getCalculateProductDtos().add(a2);
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
			OrderDetailIn od1 = new OrderDetailIn();
			od1.setProductCode("T0000155");
			od1.setProductCount(2);
			od1.setMealFlag(CommonConstant.MEAL_FLAG_1);
			od1.setGiftFlag(CommonConstant.GIFT_FLAG_0);
//			order.getOrderDetailIns().add(od1);
			System.err.println(JSON.toJSONString(this.orderRestController.calcOrder(order), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckOrderAmount() {
		try {
			CheckOrderAmountRequest request = new CheckOrderAmountRequest();
			request.setMemberCard("100000002");// 会员卡号
			request.setProductTotal(39990L);// 商品总额 单位分
			request.setAdvertBusNo(555L);// 广告业务主键
			CalculateProductDto a1 = new CalculateProductDto();
			a1.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			a1.setActivityProductBusNo(20L);// 活动商品业务主键
			a1.setActivityType(0);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			a1.setDiscountType(0);// 优惠方式：0满减，1折扣，2红包
			a1.setDiscountId(7);// 使用的优惠主键
//			a1.setCouponDiscountId(12);// 使用的优惠券折扣ID
//			a1.setMemberCouponId(1);// 使用的优惠券ID
			a1.setProductCode("10000156");// 商品code
			a1.setProductCount(2);// 商品数量
			a1.setLimitDownPrice(10000L);// 商品最低折扣价 单位分
			a1.setSalePrice(20000L);// 商品销售价 单位分
			a1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_2);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
			request.setCalculateProductDto(Arrays.asList(a1));
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
			System.err.println(this.activityClient.checkOrderAmount(request).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetLaunchManageByBusNo() {
		try {
			System.err.println(this.activityClient.getLaunchManageByBusNo(555).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOrderSubmit() {
		try {
//			{
//				  "orderNo" : "ON20210221115651626262382",
//				  "advertBusNo" : 555,
//				  "memberCard" : "100000002",
//				  "productTotal" : 39990,
//				  "orderSubmitProductDtoList" : [ {
//				    "productCode" : "10000156",
//				    "productCount" : 2,
//				    "productTotal" : 20000,
//				    "useDiscountTypeEnum" : "USE_ACTIVITY",
//				    "activityTypeEnum" : "ADVERT_LAUNCH",
//				    "activityBusNo" : 20,
//				    "activityProductBusNo" : 20,
//				    "discountTypeEnum" : "FULL_REDUCE",
//				    "discountId" : 7,
//				    "memberCouponId" : null
//				  } ],
//				  "groupId" : null,
//				  "memberLevelGrade" : 7,
//				  "userNo" : 14058,
//				  "memberCouponIdForOrder" : null
//				}
			OrderSubmitRequest request = new OrderSubmitRequest();
			request.setAdvertBusNo(555L);// 广告业务主键
			request.setMemberCard("100000002");// 会员卡号
			request.setMemberLevelGrade(7);// 会员级别
			request.setOrderNo("ON20210221115651626262382");// 订单编号
			request.setProductTotal(39990L);// 商品总额 单位分
			request.setUserNo("14058");// 操作人
			OrderSubmitProductDto dto = new OrderSubmitProductDto();
			dto.setActivityBusNo(20L);// 活动业务/会员优惠业务主键
			dto.setActivityProductBusNo(20L);// 活动商品业务主键
			dto.setActivityTypeEnum(ActivityTypeEnum.ADVERT_LAUNCH);// 优惠途径
			dto.setDiscountId(7);// 使用的优惠主键
			dto.setDiscountTypeEnum(DiscountTypeEnum.FULL_REDUCE);// 优惠方式
			dto.setProductCode("10000156");// 商品code
			dto.setProductCount(2);// 商品数量
			dto.setProductTotal(20000L);// 商品销售价,单位分
			dto.setUseDiscountTypeEnum(UseDiscountTypeEnum.USE_ACTIVITY);// 使用的优惠

			request.setOrderSubmitProductDtoList(Arrays.asList(dto));

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
			System.err.println(this.activityClient.orderSubmit(request).getData().getMemberCouponDtoList());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.net.yzl.crm.test;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.activity.model.dto.CalculateProductDto;
import cn.net.yzl.activity.model.dto.OrderSubmitProductDto;
import cn.net.yzl.activity.model.enums.ActivityTypeEnum;
import cn.net.yzl.activity.model.enums.DiscountTypeEnum;
import cn.net.yzl.activity.model.enums.UseDiscountTypeEnum;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.OrderSubmitRequest;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.order.constant.CommonConstant;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:45:55
 */
@SpringBootTest
public class ActivityClientTests {
	@Resource
	private ActivityClient activityClient;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testGetLaunchManageByBusNo() {
		try {
			System.err.println(this.activityClient.getLaunchManageByBusNo(555).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckOrderAmount() {
		try {
			CheckOrderAmountRequest request = new CheckOrderAmountRequest();
			request.setMemberCard("10000055");// 会员卡号
			request.setMemberLevelGrade(11);
			request.setProductTotal(99800L);// 商品总额 单位分
			request.setAdvertBusNo(1001L);// 广告业务主键

			CalculateProductDto a1 = new CalculateProductDto();
			a1.setActivityBusNo(null);// 活动业务/会员优惠业务主键
			a1.setActivityProductBusNo(null);// 活动商品业务主键
			a1.setActivityType(null);// 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
			a1.setDiscountType(null);// 优惠方式：0满减，1折扣，2红包
			a1.setDiscountId(null);// 使用的优惠主键
//			a1.setCouponDiscountId(12);// 使用的优惠券折扣ID
//			a1.setMemberCouponId(1);// 使用的优惠券ID
			a1.setProductCode("10000172");// 商品code
			a1.setProductCount(1);// 商品数量
			a1.setProductType(CommonConstant.MEAL_FLAG_0);
			a1.setLimitDownPrice(9900L);// 商品最低折扣价 单位分
			a1.setSalePrice(99800L);// 商品销售价 单位分
			a1.setUseDiscountType(CommonConstant.USE_DISCOUNT_TYPE_0);// 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动

			request.setCalculateProductDto(Arrays.asList(a1));

			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
			System.err.println(this.activityClient.checkOrderAmount(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOrderSubmit() {
		try {
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
			System.err.println(this.activityClient.orderSubmit(request).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.net.yzl.crm.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.lang.Tuple;
import cn.net.yzl.activity.model.dto.CalculateProductDto;
import cn.net.yzl.activity.model.dto.MemberCouponSaveDto;
import cn.net.yzl.activity.model.dto.OrderSubmitProductDto;
import cn.net.yzl.activity.model.enums.ActivityTypeEnum;
import cn.net.yzl.activity.model.enums.DiscountTypeEnum;
import cn.net.yzl.activity.model.enums.UseDiscountTypeEnum;
import cn.net.yzl.activity.model.requestModel.CalculateRequest;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.OrderSubmitRequest;
import cn.net.yzl.activity.model.responseModel.OrderSubmitResponse;
import cn.net.yzl.activity.model.responseModel.ProductPriceResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.constant.ObtainType;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.model.order.CalcOrderIn;
import cn.net.yzl.crm.model.order.CalcOrderIn.CalculateOrderProductDto;
import cn.net.yzl.crm.model.order.CalcOrderOut;
import cn.net.yzl.crm.model.order.OrderOut;
import cn.net.yzl.crm.model.order.OrderOut.Coupon;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.IOrderCommonService;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.logistics.model.vo.ExpressIndemnity;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.LeaderBoardType;
import cn.net.yzl.order.enums.PayMode;
import cn.net.yzl.order.enums.PayType;
import cn.net.yzl.order.enums.RedisKeys;
import cn.net.yzl.order.model.db.order.OrderCouponDetail;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.ehr.LeaderBoard;
import cn.net.yzl.order.model.vo.member.MemberChannel;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;
import cn.net.yzl.order.model.vo.order.OrderRequest;
import cn.net.yzl.order.model.vo.order.ReissueOrderIn;
import cn.net.yzl.order.model.vo.order.UpdateOrderIn;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单管理
 * 
 * @author zhangweiwei
 * @date 2021年1月16日,下午12:12:17
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderRestController {
	private BigDecimal bd100 = BigDecimal.valueOf(100);// 元转分

	@PostMapping("/v1/calcorder")
	@ApiOperation(value = "热线工单-购物车-计算订单金额", notes = "热线工单-购物车-计算订单金额")
	public ComResponse<CalcOrderOut> calcOrder(@RequestBody CalcOrderIn orderin) {
		if (CollectionUtils.isEmpty(orderin.getCalculateProductDtos())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		if (!StringUtils.hasText(orderin.getMemberCard())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "顾客卡号不能为空。");
		}
		// 按顾客号查询顾客信息
		Member member = this.memberFien.getMember(orderin.getMemberCard()).getData();
		if (member == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]信息", orderin.getMemberCard());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客信息。");
		}
		MemberAmountDto amount = this.memberFien.getMemberAmount(orderin.getMemberCard()).getData();
		if (amount == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]账户信息", orderin.getMemberCard());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账户信息。");
		}
		// 只匹配购买的商品或套餐，排除赠品
		List<CalculateOrderProductDto> orderproducts = orderin.getCalculateProductDtos().stream()
				.filter(p -> Integer.compare(CommonConstant.GIFT_FLAG_0, p.getGiftFlag()) == 0)
				.collect(Collectors.toList());
		// 用,拼接商品编码
		String productCodes = orderproducts.stream().map(CalculateOrderProductDto::getProductCode)
				.collect(Collectors.joining(","));
		// 商品列表
		Map<String, ProductMainDTO> productMap = Optional
				.ofNullable(this.productClient.queryByProductCodes(productCodes).getData())
				.orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
		// 套餐列表
		Map<String, ProductMealListDTO> mealMap = Optional
				.ofNullable(this.mealClient.queryByIds(productCodes).getData()).orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMealListDTO::getMealNo, Function.identity()));
		List<ProductPriceResponse> list = orderproducts.stream().map(cp -> {
			if (Integer.compare(CommonConstant.MEAL_FLAG_0, cp.getProductType()) == 0) {
				// 商品
				ProductMainDTO product = productMap.get(cp.getProductCode());
				cp.setLimitDownPrice(
						BigDecimal.valueOf(Double.valueOf(product.getLimitDownPrice())).multiply(bd100).longValue());// 商品最低折扣价,单位分
				cp.setSalePrice(BigDecimal.valueOf(Double.valueOf(product.getSalePrice())).multiply(bd100).longValue());// 商品销售价,单位分
			} else {
				// 套餐
				ProductMealListDTO meal = mealMap.get(cp.getProductCode());
				cp.setLimitDownPrice(Long.valueOf(meal.getDiscountPrice()));// 商品最低折扣价,单位分
				cp.setSalePrice(BigDecimal.valueOf(meal.getPriceD()).multiply(bd100).longValue());// 商品销售价,单位分
			}
			CalculateRequest request = new CalculateRequest();
			request.setCalculateProductDto(cp);
			request.setAdvertBusNo(orderin.getAdvertBusNo());
			request.setMemberCard(orderin.getMemberCard());
			request.setMemberLevelGrade(member.getMGradeId());
			log.info("调用计算金额接口：{}", this.toJsonString(request));
			return this.activityClient.calculate(request).getData();
		}).collect(Collectors.toList());
		// 商品数量*商品价格，然后求和，计算出订单总额
		long totalAll = orderproducts.stream().mapToLong(m -> m.getProductCount() * m.getSalePrice()).sum();
		// 对每一件商品经过DMC接口算出优惠价，然后求和，计算出商品优惠总价
		double productTotal = list.stream().mapToDouble(m -> m.getProductTotal().doubleValue()).sum();
		// 优惠券+活动优惠的总价
		double amountCoupon = list.stream()
				.mapToDouble(m -> Optional.ofNullable(m.getActivityDiscountPrice()).orElse(BigDecimal.ZERO)
						.add(Optional.ofNullable(m.getCouponDiscountPrice()).orElse(BigDecimal.ZERO)).doubleValue())
				.sum();
		double total = productTotal;
		if (orderin.getAmountStored().compareTo(BigDecimal.ZERO) > 0) {
			double am = orderin.getAmountStored().doubleValue();
			if (Double.compare(total, am) >= 0) {
				total -= am;
			} else {
				total = 0;
			}
		}
		return ComResponse.success(new CalcOrderOut(BigDecimal.valueOf(totalAll).divide(bd100).doubleValue(), total,
				amountCoupon, 0d, orderin.getAmountStored().doubleValue(), productTotal));
	}

	/**
	 * Object转JSON字符串
	 * 
	 * @param <T>
	 * @param object
	 * @return JSON字符串
	 * @author zhangweiwei
	 * @date 2021年2月23日,下午8:53:30
	 */
	private <T> String toJsonString(T object) {
		try {
			return this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}

	@PostMapping("/v1/submitorder")
	@ApiOperation(value = "热线工单-购物车-提交订单", notes = "热线工单-购物车-提交订单")
	public ComResponse<OrderOut> submitOrder(@RequestBody OrderIn orderin) {
		OrderM orderm = new OrderM();// 订单信息
		this.initOrder(orderm);
		Optional.ofNullable(orderin.getAmountStored())
				.ifPresent(c -> orderm.setAmountStored(c.multiply(bd100).intValue()));
		// 如果订单里没有商品
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按顾客号查询顾客信息
		GeneralResult<Member> mresult = this.memberFien.getMember(orderin.getMemberCardNo());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresult.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用查询顾客信息接口异常：%s", mresult.getMessage()));
		}
		Member member = mresult.getData();
		if (member == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]信息", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客信息。");
		}
		orderm.setMemberLevelBefor(member.getM_grade_code());// 单前顾客级别
		orderm.setMemberTypeBefor(member.getMember_type());// 单前顾客类型
		orderm.setMemberName(member.getMember_name());// 顾客姓名
		orderm.setMemberCardNo(orderin.getMemberCardNo());// 顾客卡号
		// 按顾客号查询顾客收获地址
		ComResponse<List<ReveiverAddressDto>> raresponse = this.memberFien
				.getReveiverAddress(orderin.getMemberCardNo());
		// 如果调用服务异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(raresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用查询顾客收货地址接口异常：%s", raresponse.getMessage()));
		}
		List<ReveiverAddressDto> reveiverAddresses = raresponse.getData();
		if (CollectionUtils.isEmpty(reveiverAddresses)) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]收货地址", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客收货地址。");
		}
		ReveiverAddressDto reveiverAddress = reveiverAddresses.stream()
				.filter(p -> p.getId().equals(orderin.getReveiverAddressNo())).findFirst().orElse(null);
		if (reveiverAddress == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]收货地址", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客收货地址。");
		}
		// 按顾客号查询顾客账号
		ComResponse<MemberAmountDto> maresponse = this.memberFien.getMemberAmount(orderin.getMemberCardNo());
		// 如果调用服务异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(maresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用查询顾客账号接口异常：%s", maresponse.getMessage()));
		}
		MemberAmountDto account = maresponse.getData();
		if (account == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]账号", orderin.getMemberCardNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
		}
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(orderm.getStaffCode());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", orderm.getStaffCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用员工编号获取员工基本信息接口异常：%s", sresponse.getMessage()));
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]信息", orderm.getStaffCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6));// 使用redis生成订单号
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(String.valueOf(staffInfo.getDepartId()));// 下单坐席所属部门id
		orderm.setUpdateCode(orderm.getStaffCode());// 更新人编号
		orderm.setUpdateName(staffInfo.getName());// 更新人姓名
		orderm.setWorkCode(staffInfo.getWorkCode());// 职场id
		orderm.setWorkCodeStr(staffInfo.getWorkCodeStr());// 职场
		// 按部门id查询部门信息
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(staffInfo.getDepartId());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用id获取部门信息接口异常：%s", dresponse.getMessage()));
		}
		DepartDto depart = dresponse.getData();
		if (depart == null) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]所在部门[{}]的财务归属", orderm.getStaffCode(), staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席的财务归属。");
		}
		orderm.setFinancialOwner(depart.getFinanceDepartId());// 下单坐席财务归属部门id
		orderm.setFinancialOwnerName(depart.getFinanceDepartName());// 下单坐席财务归属部门名称
		// 如果广告ID不为空
		if (orderin.getAdvertBusNo() != null) {
			orderm.setAdvisorNo(orderin.getAdvertBusNo().intValue());// 广告id
			// 查询广告
			ComResponse<LaunchManageDto> response = this.activityClient.getLaunchManageByBusNo(orderm.getAdvisorNo());
			LaunchManageDto dto = response.getData();
			if (dto != null) {
				orderm.setAdvisorName(dto.getAdvertName());// 广告名称
				orderm.setMediaName(dto.getMediaName());// 媒介名称
			}
		}
		// 组装校验订单金额参数
		CheckOrderAmountRequest checkOrderAmountRequest = this.getCheckOrderAmountRequest(orderin, member);
		log.info("调用校验订单金额接口：{}", this.toJsonString(checkOrderAmountRequest));
		// 调用校验订单金额接口
		ComResponse<List<ProductPriceResponse>> ppresponse = this.activityClient
				.checkOrderAmount(checkOrderAmountRequest);
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(ppresponse.getCode())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用校验订单金额接口异常：%s", ppresponse.getMessage()));
		}
		List<ProductPriceResponse> productPriceList = ppresponse.getData();
		if (CollectionUtils.isEmpty(productPriceList)) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品或套餐的优惠价格");
		}
		// key为商品编码，value为商品优惠价格对象
		Map<String, ProductPriceResponse> productPriceMap = productPriceList.stream()
				.collect(Collectors.toMap(ProductPriceResponse::getProductCode, Function.identity()));
		// 按套餐和非套餐对订单明细进行分组，key为套餐标识，value为订单明细集合
		Map<Integer, List<OrderDetailIn>> orderdetailMap = orderin.getOrderDetailIns().stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getProductType));
		// 获取非套餐，也就是纯商品
		List<OrderDetailIn> orderProductList = orderdetailMap.get(CommonConstant.MEAL_FLAG_0);
		// 收集套餐关联的商品和非套餐商品
		List<OrderDetail> orderdetailList = new ArrayList<>();
		// 收集商品或套餐优惠信息
		List<OrderCouponDetail> coupondetailList = new ArrayList<>();
		// 收集每类商品的库存，key为商品编码，value为商品库存
		Map<String, Integer> productStockMap = new HashMap<>();
		// 收集订单明细里商品的购买数量
		List<Tuple> tuples = new ArrayList<>();
		AtomicInteger seq = new AtomicInteger(10);// 循环序列
		// 如果有非套餐信息
		if (!CollectionUtils.isEmpty(orderProductList)) {
			// 收集商品编码
			List<String> productCodeList = orderProductList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接商品编码
			String productCodes = productCodeList.stream().collect(Collectors.joining(","));
			// 根据拼接后的商品编码查询商品列表
			ComResponse<List<ProductMainDTO>> presponse = this.productClient.queryByProductCodes(productCodes);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(presponse.getCode())) {
				log.error("热线工单-购物车-提交订单>>{}", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用商品编码查询商品列表接口异常：%s", presponse.getMessage()));
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("热线工单-购物车-提交订单>>找不到商品[{}]信息", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			if (Integer.compare(plist.size(), productCodeList.size()) != 0) {
				log.error("热线工单-购物车-提交订单>>订单的商品编码总数[{}]与商品查询接口的商品编码总数[{}]不一致", productCodeList.size(), plist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的商品部分已下架。");
			}
			// 将商品编码作为key，商品对象作为value
			Map<String, ProductMainDTO> pmap = plist.stream()
					.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
			// 组装订单明细信息
			for (OrderDetailIn in : orderProductList) {
				ProductMainDTO p = pmap.get(in.getProductCode());
				// 如果商品已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, p.getStatus()) == 0) {
					log.error("热线工单-购物车-提交订单>>该商品已下架>>{}", p);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品已下架。");
				}
				OrderDetail od = new OrderDetail();
				// 按主订单号生成订单明细编号
				od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
				od.setOrderNo(orderm.getOrderNo());// 订单编号
				od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
				od.setStaffCode(orderm.getStaffCode());// 创建人编号
				od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
				od.setMemberName(orderm.getMemberName());// 顾客姓名
				od.setDepartId(orderm.getDepartId());// 部门表唯一标识
				od.setWorkCode(orderm.getWorkCode());// 职场id
				od.setWorkCodeStr(orderm.getWorkCodeStr());// 职场
				od.setWorkOrderType(orderin.getWorkOrderType());// 工单类型
				od.setGiftFlag(in.getGiftFlag());// 是否赠品
				od.setMealFlag(CommonConstant.MEAL_FLAG_0);// 不是套餐
				od.setProductCode(p.getProductCode());// 商品唯一标识
				od.setProductNo(p.getProductNo());// 商品编码
				od.setProductName(p.getName());// 商品名称
				od.setProductBarCode(p.getBarCode());// 产品条形码
				od.setProductUnitPrice(BigDecimal.valueOf(Double.valueOf(p.getSalePrice())).multiply(bd100).intValue());// 商品单价，单位分
				od.setProductCount(in.getProductCount());// 商品数量
				od.setUnit(p.getUnit());// 单位
				od.setSpec(String.valueOf(p.getTotalUseNum()));// 商品规格
				od.setPackageUnit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
				// 如果是非赠品
				if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
					od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
					od.setCash(productPriceMap.get(p.getProductCode()).getProductTotal().multiply(bd100).intValue());// 应收金额，单位分
				} else {// 如果是赠品，将金额设置为0
					od.setTotal(0);// 实收金额，单位分
					od.setCash(0);// 应收金额，单位分
				}
				orderdetailList.add(od);
				ProductPriceResponse pp = productPriceMap.get(p.getProductCode());
				if (pp != null) {
					switch (pp.getUseDiscountType()) {
					case CommonConstant.USE_DISCOUNT_TYPE_1:// 使用的优惠：1优惠券
						coupondetailList.add(this.getOrderCouponDetail1(seq, in, od, pp));
						break;
					case CommonConstant.USE_DISCOUNT_TYPE_2:// 使用的优惠：2优惠活动
						coupondetailList.add(this.getOrderCouponDetail2(seq, in, od, pp));
						break;
					case CommonConstant.USE_DISCOUNT_TYPE_3:// 使用的优惠：3优惠券+优惠活动
						coupondetailList.add(this.getOrderCouponDetail1(seq, in, od, pp));
						coupondetailList.add(this.getOrderCouponDetail2(seq, in, od, pp));
						break;
					default:
						break;
					}
				}
			}
		}
		// 获取套餐
		List<OrderDetailIn> ordermealList = orderdetailMap.get(CommonConstant.MEAL_FLAG_1);
		// 如果有套餐信息
		if (!CollectionUtils.isEmpty(ordermealList)) {
			// 收集套餐编码
			List<String> mealNoList = ordermealList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接套餐编码
			String mealNos = mealNoList.stream().collect(Collectors.joining(","));
			// 根据拼接后的套餐编码查询套餐列表
			ComResponse<List<ProductMealListDTO>> mresponse = this.mealClient.queryByIds(mealNos);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresponse.getCode())) {
				log.error("热线工单-购物车-提交订单>>{}", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用套餐编码查询套餐里商品列表接口异常：%s", mresponse.getMessage()));
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("热线工单-购物车-提交订单>>找不到套餐[{}]信息", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			if (Integer.compare(mlist.size(), mealNoList.size()) != 0) {
				log.error("热线工单-购物车-提交订单>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的套餐部分已下架。");
			}
			// key为套餐编码，value为套餐对象
			Map<String, OrderDetailIn> ordermealMap = ordermealList.stream()
					.collect(Collectors.toMap(OrderDetailIn::getProductCode, Function.identity()));
			// 统计订单明细中的每个套餐的总数，key为套餐编码，value为购买套餐总数
			Map<String, Integer> mealCountMap = ordermealList.stream()
					.collect(Collectors.groupingBy(OrderDetailIn::getProductCode)).entrySet().stream()
					.collect(Collectors.toMap(Entry::getKey,
							v -> v.getValue().stream().mapToInt(OrderDetailIn::getProductCount).sum()));
			for (ProductMealListDTO meal : mlist) {
				// 如果套餐已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, meal.getStatus()) == 0) {
					log.error("热线工单-购物车-提交订单>>该套餐已下架>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐已下架。");
				}
				// 如果套餐里没有商品
				if (CollectionUtils.isEmpty(meal.getMealProductList())) {
					log.error("热线工单-购物车-提交订单>>该套餐没有包含商品信息>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐没有包含商品信息。");
				}
				// 套餐数量
				Integer mealCount = mealCountMap.get(meal.getMealNo());
				// 套餐价，单位分
				BigDecimal mealPrice = BigDecimal.valueOf(meal.getPriceD()).multiply(bd100);
				// 套餐优惠价，单位分
				ProductPriceResponse pp = productPriceMap.get(meal.getMealNo());
				if (pp != null) {
					switch (pp.getUseDiscountType()) {
					case CommonConstant.USE_DISCOUNT_TYPE_1:// 使用的优惠：1优惠券
						coupondetailList.add(
								this.getOrderCouponDetail1(orderm, seq, ordermealMap.get(meal.getMealNo()), meal, pp));
						break;
					case CommonConstant.USE_DISCOUNT_TYPE_2:// 使用的优惠：2优惠活动
						coupondetailList.add(
								this.getOrderCouponDetail2(orderm, seq, ordermealMap.get(meal.getMealNo()), meal, pp));
						break;
					case CommonConstant.USE_DISCOUNT_TYPE_3:// 使用的优惠：3优惠券+优惠活动
						coupondetailList.add(
								this.getOrderCouponDetail1(orderm, seq, ordermealMap.get(meal.getMealNo()), meal, pp));
						coupondetailList.add(
								this.getOrderCouponDetail2(orderm, seq, ordermealMap.get(meal.getMealNo()), meal, pp));
						break;
					default:
						break;
					}
				}
				BigDecimal mealDiscountPrice = pp.getProductTotal().multiply(bd100);
				// 组装订单明细信息
				List<OrderDetail> result = meal.getMealProductList().stream().map(in -> {
					OrderDetail od = new OrderDetail();
					// 按主订单号生成订单明细编号
					od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
					od.setOrderNo(orderm.getOrderNo());// 订单编号
					od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
					od.setStaffCode(orderm.getStaffCode());// 创建人编号
					od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
					od.setMemberName(orderm.getMemberName());// 顾客姓名
					od.setDepartId(orderm.getDepartId());// 部门表唯一标识
					od.setWorkCode(orderm.getWorkCode());// 职场id
					od.setWorkCodeStr(orderm.getWorkCodeStr());// 职场
					od.setWorkOrderType(orderin.getWorkOrderType());// 工单类型
					od.setGiftFlag(in.getMealGiftFlag());// 是否赠品
					od.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是套餐
					od.setMealName(meal.getName());// 套餐名称
					od.setMealNo(meal.getMealNo());// 套餐唯一标识
					od.setMealCount(mealCount);// 套餐数量
					od.setMealPrice(mealPrice.intValue());// 套餐价格，单位分
					od.setProductCode(in.getProductCode());// 商品唯一标识
					od.setProductNo(in.getProductNo());// 商品编码
					od.setProductName(in.getName());// 商品名称
					od.setProductBarCode(in.getBarCode());// 产品条形码
					od.setProductUnitPrice(in.getSalePrice());// 商品单价，单位分
					od.setProductCount(in.getProductNum() * mealCount);// 商品数量*套餐数量
					od.setUnit(in.getUnit());// 单位
					od.setSpec(String.valueOf(in.getTotalUseNum()));// 商品规格
					od.setPackageUnit(in.getPackagingUnit());// 包装单位
					productStockMap.put(od.getProductCode(), in.getStock());// 库存
					tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList());
				// 套餐商品总金额
				BigDecimal orderdetailTotal = BigDecimal.valueOf(result.stream().mapToInt(OrderDetail::getTotal).sum());
				orderdetailList.addAll(result.stream().map(od -> {
					BigDecimal price = mealPrice.multiply(BigDecimal.valueOf(od.getProductUnitPrice()))
							.multiply(BigDecimal.valueOf(mealCount))
							.divide(orderdetailTotal, 0, BigDecimal.ROUND_HALF_UP);
					BigDecimal discountPrice = mealDiscountPrice.multiply(BigDecimal.valueOf(od.getProductUnitPrice()))
							.multiply(BigDecimal.valueOf(mealCount))
							.divide(orderdetailTotal, 0, BigDecimal.ROUND_HALF_UP);
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setProductUnitPrice(price.intValue());
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(discountPrice.intValue() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setProductUnitPrice(0);
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList()));
			}
		}
		// 商品数量*商品价，然后再求和
		orderm.setTotalAll(orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
		orderm.setCash(orderdetailList.stream().mapToInt(OrderDetail::getCash).sum());
		orderm.setTotal(orderm.getCash());
		orderm.setSpend(orderm.getCash());
		orderm.setAmountCoupon(orderm.getTotalAll() - orderin.getProductTotal().multiply(bd100).intValue());
		// 统计订单明细中的每类商品的总数，key为商品编码，value为购买商品总数
		Map<String, Integer> productCountMap = new HashMap<>();
		tuples.stream().forEach(p -> productCountMap.merge(p.get(0), p.get(1), Integer::sum));
		// 校验订单购买商品的总数是否超出库存数
		Set<Entry<String, Integer>> entrySet = productCountMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			Integer pstock = productStockMap.get(entry.getKey());// 取出商品库存
			// 如果购买商品总数大于商品库存
			if (entry.getValue() > pstock) {
				log.error("热线工单-购物车-提交订单>>该订单[{}]商品[{}]购买总数[{}]大于库存总数[{}]", orderm.getOrderNo(), entry.getKey(),
						entry.getValue(), pstock);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品库存不足。");
			}
		}
		orderm.setWorkOrderNo(orderin.getWorkOrderNo());// 工单号
		orderm.setWorkBatchNo(orderin.getWorkBatchNo());// 工单流水号
		orderm.setPayType(orderin.getPayType());// 支付方式
		orderm.setOrderNature(CommonConstant.ORDER_NATURE_F);// 非免审
		// 如果是款到发货
		if (Integer.compare(CommonConstant.PAY_TYPE_1, orderin.getPayType()) == 0) {
			orderm.setPayStatus(CommonConstant.PAY_STATUS_0);// 未收款
		}
		orderm.setWorkOrderType(orderin.getWorkOrderType());// 工单类型
		orderm.setRemark(orderin.getRemark());// 订单备注
		orderm.setReveiverAddressNo(orderin.getReveiverAddressNo());// 配送地址唯一标识
		orderm.setReveiverAddress(reveiverAddress.getMemberAddress());// 收货人地址
		orderm.setReveiverName(reveiverAddress.getMemberName());// 收货人姓名
		orderm.setReveiverTelphoneNo(reveiverAddress.getMemberMobile());// 收货人电话
		orderm.setReveiverProvince(String.valueOf(reveiverAddress.getMemberProvinceNo()));// 省份编码
		orderm.setReveiverProvinceName(reveiverAddress.getMemberProvinceName());// 省份名称
		orderm.setReveiverCity(String.valueOf(reveiverAddress.getMemberCityNo()));// 城市编码
		orderm.setReveiverCityName(reveiverAddress.getMemberCityName());// 城市名称
		orderm.setReveiverArea(String.valueOf(reveiverAddress.getMemberCountyNo()));// 区县编码
		orderm.setReveiverAreaName(reveiverAddress.getMemberCountyName());// 区县名称
		orderm.setMediaChannel(orderin.getMediaChannel());// 媒介渠道
		orderm.setMediaName(orderin.getMediaName());// 媒介名称
		orderm.setMediaNo(orderin.getMediaNo());// 媒介唯一标识
		orderm.setMediaType(orderin.getMediaType());// 媒介类型
		orderm.setMemberTelphoneNo(orderin.getMemberTelphoneNo());// 顾客电话
		// 组装扣减库存参数
		OrderProductVO orderProduct = new OrderProductVO();
		orderProduct.setOrderNo(orderm.getOrderNo());// 订单编号
		orderProduct.setProductReduceVOS(entrySet.stream().map(m -> {
			ProductReduceVO vo = new ProductReduceVO();
			vo.setNum(m.getValue());// 商品数量
			vo.setProductCode(m.getKey());// 商品编号
			vo.setOrderNo(orderm.getOrderNo());// 订单编号
			return vo;
		}).collect(Collectors.toList()));
		// 调用扣减库存服务接口
		ComResponse<?> productReduce = this.productClient.productReduce(orderProduct);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(productReduce.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", this.toJsonString(orderProduct));
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用扣减库存接口异常：%s", productReduce.getMessage()));
		}
		if (this.hasAmountStored(orderin)) {
			// 组装顾客账户消费参数
			MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
			memberAmountDetail.setDiscountMoney(orderm.getTotal());// 订单总金额，单位分
			memberAmountDetail.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
			memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_2);// 消费
			memberAmountDetail.setOrderNo(orderm.getOrderNo());// 订单编号
			memberAmountDetail.setRemark("热线工单-购物车-提交订单:坐席下单");// 备注
			// 调用顾客账户消费服务接口
			ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
				log.error("热线工单-购物车-提交订单>>{}>>{}", orderm.getMemberCardNo(), customerAmountOperation);
				// 恢复库存
				ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
				// 如果调用服务接口失败
				if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
					log.error("热线工单-购物车-提交订单>>{}", increaseStock);
					this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL,
							ProductClient.INCREASE_STOCK_URL, orderm.getStaffCode(), orderm.getOrderNo());
				}
				return ComResponse.fail(ResponseCodeEnums.ERROR, customerAmountOperation.getMessage());
			}
		}
		log.info("订单信息: {}", this.toJsonString(orderm));
		log.info("订单明细信息: {}", this.toJsonString(orderdetailList));
		// 组装提交订单送积分和优惠券参数
		OrderSubmitRequest orderSubmitRequest = this.getOrderSubmitRequest(orderin, member, orderm);
		log.info("调用提交订单送积分和优惠券接口：{}", this.toJsonString(orderSubmitRequest));
		// 提交订单送积分和优惠券
		ComResponse<OrderSubmitResponse> orderSubmit = this.activityClient.orderSubmit(orderSubmitRequest);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(orderSubmit.getCode())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用提交订单送积分和优惠券接口异常：%s", orderSubmit.getMessage()));
		}
		OrderSubmitResponse orderSubmitResponse = orderSubmit.getData();
		if (orderSubmitResponse == null) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "赠送积分、优惠券异常");
		}
		orderm.setReturnPointsDeduction(orderSubmitResponse.getIntegral());// 本次送的积分
		// 收集订单返回信息
		OrderOut orderout = new OrderOut();
		// 如果本次下单送的优惠券不为空
		List<MemberCouponSaveDto> memberCouponDtos = orderSubmitResponse.getMemberCouponSaveDtoList();
		if (!CollectionUtils.isEmpty(memberCouponDtos)) {
			// key为商品编码，value为订单明细对象
			Map<String, List<OrderDetail>> odMap = orderdetailList.stream()
					.collect(Collectors.groupingBy(OrderDetail::getProductCode));
			for (MemberCouponSaveDto memberCoupon : memberCouponDtos) {
				OrderCouponDetail cd = new OrderCouponDetail();
				OrderDetail od = odMap.get(memberCoupon.getProductCode()).get(0);
				// 优惠使用记录表业务主键
				cd.setOrderCouponDetailNo(String.format("%s%s", od.getOrderDetailCode(), seq.incrementAndGet()));
				cd.setOrderDetailCode(od.getOrderDetailCode());// 订单明细表业务标识
				cd.setOrderNo(od.getOrderNo());// 订单号
				cd.setMealFlag(od.getMealFlag());// 是否套餐
				cd.setProductCode(memberCoupon.getProductCode());// 商品唯一标识
				cd.setActivityType(memberCoupon.getSourceType());// 优惠途径
				cd.setCouponCode(memberCoupon.getCouponBusNo().intValue());// 优惠编号
				cd.setCouponName(memberCoupon.getCouponName());// 优惠券名称
//				cd.setCouponType(pp.getDiscountType());// 优惠方式
//				cd.setCouponAmt(pp.getActivityDiscountPrice().multiply(bd100).intValue());// 优惠金额 单位分
				cd.setCouponMode(CommonConstant.COUPON_MODE_1);// 活动类型
				cd.setCouponDirection(CommonConstant.COUPON_DIRECTION_2);// 优惠方向
				coupondetailList.add(cd);
				orderout.getCoupons().add(new Coupon(memberCoupon.getStartData(), memberCoupon.getEndData(),
						memberCoupon.getCouponName()));
			}
		}
		// 调用创建订单服务接口
		ComResponse<?> submitOrder = this.orderFeignClient
				.submitOrder(new OrderRequest(orderm, orderdetailList, coupondetailList));
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(submitOrder.getCode())) {
			log.error("热线工单-购物车-提交订单>>创建订单失败[订单号：{}]", orderm.getOrderNo());
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("热线工单-购物车-提交订单>>{}", increaseStock);
				this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL, ProductClient.INCREASE_STOCK_URL,
						orderm.getStaffCode(), orderm.getOrderNo());
			}
			if (this.hasAmountStored(orderin)) {
				// 组装顾客账户退回参数
				MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
				memberAmountDetail.setDiscountMoney(orderm.getTotal());// 订单总金额，单位分
				memberAmountDetail.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
				memberAmountDetail.setOrderNo(orderm.getOrderNo());// 订单编号
				memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 退回
				memberAmountDetail.setRemark("热线工单-购物车-提交订单:退回金额");// 备注
				ComResponse<?> customerAmountOperation2 = this.memberFien.customerAmountOperation(memberAmountDetail);
				// 如果调用服务接口失败
				if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation2.getCode())) {
					log.error("热线工单-购物车-提交订单>>{}", customerAmountOperation2);
					this.orderCommonService.insert(memberAmountDetail, MemberFien.SUFFIX_URL,
							MemberFien.CUSTOMER_AMOUNT_OPERATION_URL, orderm.getStaffCode(), orderm.getOrderNo());
				}
			}
			return ComResponse.fail(ResponseCodeEnums.ERROR, "提交订单失败，请稍后重试。");
		}
		log.info("热线工单-购物车-提交订单>>创建订单成功[订单号：{}]", orderm.getOrderNo());
		// 顾客管理-处理下单时更新顾客信息
		OrderCreateInfoVO orderCreateInfoVO = new OrderCreateInfoVO();
		orderCreateInfoVO.setCreateTime(orderm.getCreateTime());// 下单时间
		orderCreateInfoVO.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
		orderCreateInfoVO.setOrderNo(orderm.getOrderNo());// 订单编号
		orderCreateInfoVO.setStaffNo(orderm.getStaffCode());// 下单坐席编号
		ComResponse<Boolean> createUpdateMember = this.memberFien.dealOrderCreateUpdateMemberData(orderCreateInfoVO);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(createUpdateMember.getCode())) {
			log.error("热线工单-购物车-提交订单>>{}", createUpdateMember);
			this.orderCommonService.insert(createUpdateMember, MemberFien.SUFFIX_URL,
					MemberFien.DEAL_ORDER_CREATE_UPDATE_MEMBER_DATA_URL, orderm.getStaffCode(), orderm.getOrderNo());
		}
		// 再次调用顾客账户余额
		maresponse = this.memberFien.getMemberAmount(orderm.getMemberCardNo());
		orderout.setOrderNo(orderm.getOrderNo());
		orderout.setReveiverAddress(orderm.getReveiverAddress());
		orderout.setReveiverName(orderm.getReveiverName());
		orderout.setReveiverTelphoneNo(orderm.getReveiverTelphoneNo());
		orderout.setTotal(BigDecimal.valueOf(orderm.getTotal()).divide(bd100));
		orderout.setTotalMoney(BigDecimal.valueOf(maresponse.getData().getTotalMoney()).divide(bd100));
		orderout.setOrderTime(orderm.getCreateTime());
		orderout.setReturnPointsDeduction(orderm.getReturnPointsDeduction());
		return ComResponse.success(orderout);
	}

	/**
	 * 优惠途径：0广告投放，1会员优惠，2当前坐席的任务优惠
	 * 
	 * @param activityType
	 * @return {@link ActivityTypeEnum}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午9:36:27
	 */
	private ActivityTypeEnum getActivityTypeEnum(Integer activityType) {
		if (activityType == null) {
			return null;
		}
		return Arrays.stream(ActivityTypeEnum.values()).filter(p -> Integer.compare(p.getCode(), activityType) == 0)
				.findFirst().orElse(null);
	}

	/**
	 * 优惠方式：0满减，1折扣，2红包
	 * 
	 * @param discountType
	 * @return {@link DiscountTypeEnum}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午9:36:31
	 */
	private DiscountTypeEnum getDiscountTypeEnum(Integer discountType) {
		if (discountType == null) {
			return null;
		}
		return Arrays.stream(DiscountTypeEnum.values()).filter(p -> Integer.compare(p.getCode(), discountType) == 0)
				.findFirst().orElse(null);
	}

	/**
	 * 使用的优惠：0不使用，1优惠券，2优惠活动，3优惠券+优惠活动
	 * 
	 * @param useDiscountType
	 * @return {@link UseDiscountTypeEnum}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午9:36:37
	 */
	private UseDiscountTypeEnum getUseDiscountTypeEnum(Integer useDiscountType) {
		if (useDiscountType == null) {
			return null;
		}
		return Arrays.stream(UseDiscountTypeEnum.values())
				.filter(p -> Integer.compare(p.getCode(), useDiscountType) == 0).findFirst().orElse(null);
	}

	/**
	 * 组装优惠使用记录数据
	 * 
	 * @param orderm    {@link OrderM}
	 * @param seq       {@link AtomicInteger}
	 * @param ordermeal {@link OrderDetailIn}
	 * @param meal      {@link ProductMealListDTO}
	 * @param pp        {@link ProductPriceResponse}
	 * @return {@link OrderCouponDetail}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午4:14:55
	 */
	private OrderCouponDetail getOrderCouponDetail2(OrderM orderm, AtomicInteger seq, OrderDetailIn ordermeal,
			ProductMealListDTO meal, ProductPriceResponse pp) {
		OrderCouponDetail cd = new OrderCouponDetail();
		// 优惠使用记录表业务主键
		cd.setOrderCouponDetailNo(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
//		cd.setOrderDetailCode(od.getOrderDetailCode());// 订单明细表业务标识
		cd.setOrderNo(orderm.getOrderNo());// 订单号
		cd.setProductCode(meal.getMealNo());// 商品唯一标识
		cd.setActivityType(pp.getActivityType());// 优惠途径
		cd.setCouponCode(ordermeal.getDiscountId());// 优惠编号
		cd.setCouponType(pp.getDiscountType());// 优惠方式
		cd.setCouponMode(CommonConstant.COUPON_MODE_2);// 活动类型
		cd.setCouponAmt(pp.getActivityDiscountPrice().multiply(bd100).intValue());// 优惠金额 单位分
		cd.setCouponDirection(CommonConstant.COUPON_DIRECTION_1);// 优惠方向
		cd.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是否套餐
		return cd;
	}

	/**
	 * 组装优惠使用记录数据
	 * 
	 * @param orderm    {@link OrderM}
	 * @param seq       {@link AtomicInteger}
	 * @param ordermeal {@link OrderDetailIn}
	 * @param meal      {@link ProductMealListDTO}
	 * @param pp        {@link ProductPriceResponse}
	 * @return {@link OrderCouponDetail}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午4:09:46
	 */
	private OrderCouponDetail getOrderCouponDetail1(OrderM orderm, AtomicInteger seq, OrderDetailIn ordermeal,
			ProductMealListDTO meal, ProductPriceResponse pp) {
		OrderCouponDetail cd = new OrderCouponDetail();
		// 优惠使用记录表业务主键
		cd.setOrderCouponDetailNo(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
//		cd.setOrderDetailCode(od.getOrderDetailCode());// 订单明细表业务标识
		cd.setOrderNo(orderm.getOrderNo());// 订单号
		cd.setProductCode(meal.getMealNo());// 商品唯一标识
		cd.setActivityType(pp.getActivityType());// 优惠途径
		cd.setCouponCode(ordermeal.getDiscountId());// 优惠编号
		cd.setCouponType(pp.getDiscountType());// 优惠方式
		cd.setCouponMode(CommonConstant.COUPON_MODE_1);// 活动类型
		cd.setCouponAmt(pp.getCouponDiscountPrice().multiply(bd100).intValue());// 优惠金额 单位分
		cd.setCouponDirection(CommonConstant.COUPON_DIRECTION_1);// 优惠方向
		cd.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是否套餐
		return cd;
	}

	/**
	 * 组装校验订单金额接口参数
	 * 
	 * @param orderin {@link OrderIn}
	 * @param member  {@link Member}
	 * @return {@link CheckOrderAmountRequest}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午3:47:29
	 */
	private CheckOrderAmountRequest getCheckOrderAmountRequest(OrderIn orderin, Member member) {
		CheckOrderAmountRequest request = new CheckOrderAmountRequest();
		request.setAdvertBusNo(orderin.getAdvertBusNo());// 广告业务主键
		request.setCouponDiscountIdForOrder(orderin.getCouponDiscountIdForOrder());// 使用的优惠券折扣ID,针对订单使用的
		request.setMemberCard(orderin.getMemberCardNo());// 会员卡号
		request.setMemberLevelGrade(member.getMGradeId());// 会员级别
		request.setMemberCouponIdForOrder(orderin.getMemberCouponIdForOrder());// 使用的优惠券ID,针对订单使用的
		request.setProductTotal(orderin.getProductTotal().multiply(bd100).longValue());// 商品总额，订单中所有商品,单位分
		// 只匹配购买的商品或套餐，排除赠品
		List<OrderDetailIn> orderdetailins = orderin.getOrderDetailIns().stream()
				.filter(p -> Integer.compare(CommonConstant.GIFT_FLAG_0, p.getGiftFlag()) == 0)
				.collect(Collectors.toList());
		// 用,拼接商品编码
		String productCodes = orderdetailins.stream().map(OrderDetailIn::getProductCode)
				.collect(Collectors.joining(","));
		// 商品列表
		Map<String, ProductMainDTO> productMap = Optional
				.ofNullable(this.productClient.queryByProductCodes(productCodes).getData())
				.orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
		// 套餐列表
		Map<String, ProductMealListDTO> mealMap = Optional
				.ofNullable(this.mealClient.queryByIds(productCodes).getData()).orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMealListDTO::getMealNo, Function.identity()));
		// 商品相关信息
		request.setCalculateProductDto(orderdetailins.stream().map(m -> {
			CalculateProductDto dto = new CalculateProductDto();
			dto.setActivityBusNo(m.getActivityBusNo());// 活动业务/会员优惠业务主键
			dto.setActivityProductBusNo(m.getActivityProductBusNo());// 活动商品业务主键
			dto.setActivityType(m.getActivityType());// 优惠途径
			dto.setCouponDiscountId(m.getCouponDiscountId());// 使用的优惠券折扣ID
			dto.setDiscountId(m.getDiscountId());// 使用的优惠主键
			dto.setDiscountType(m.getDiscountType());// 优惠方式
			dto.setMemberCouponId(m.getMemberCouponId());// 使用的优惠券ID
			dto.setProductCode(m.getProductCode());// 商品code
			dto.setProductCount(m.getProductCount());// 商品数量
			dto.setProductType(m.getProductType());// 商品类型
			if (Integer.compare(CommonConstant.MEAL_FLAG_0, m.getProductType()) == 0) {
				// 商品
				ProductMainDTO product = productMap.get(m.getProductCode());
				dto.setLimitDownPrice(
						BigDecimal.valueOf(Double.valueOf(product.getLimitDownPrice())).multiply(bd100).longValue());// 商品最低折扣价,单位分
				dto.setSalePrice(
						BigDecimal.valueOf(Double.valueOf(product.getSalePrice())).multiply(bd100).longValue());// 商品销售价,单位分
			} else {
				// 套餐
				ProductMealListDTO meal = mealMap.get(m.getProductCode());
				dto.setLimitDownPrice(Long.valueOf(meal.getDiscountPrice()));// 商品最低折扣价,单位分
				dto.setSalePrice(BigDecimal.valueOf(meal.getPriceD()).multiply(bd100).longValue());// 商品销售价,单位分
			}
			dto.setUseDiscountType(m.getUseDiscountType());// 使用的优惠
			return dto;
		}).collect(Collectors.toList()));
		return request;
	}

	/**
	 * 组装提交订单送积分和优惠券接口参数
	 * 
	 * @param orderin {@link OrderIn}
	 * @param member  {@link Member}
	 * @param orderm  {@link OrderM}
	 * @return {@link OrderSubmitRequest}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午9:52:00
	 */
	private OrderSubmitRequest getOrderSubmitRequest(OrderIn orderin, Member member, OrderM orderm) {
		OrderSubmitRequest request = new OrderSubmitRequest();
		request.setAdvertBusNo(orderin.getAdvertBusNo());// 广告业务主键
		request.setMemberCard(orderin.getMemberCardNo());// 会员卡号
		request.setMemberCouponIdForOrder(orderin.getMemberCouponIdForOrder());// 使用的优惠券ID,针对订单使用的
		request.setMemberLevelGrade(member.getMGradeId());// 会员级别
		request.setOrderNo(orderm.getOrderNo());// 订单编号
		request.setProductTotal(orderin.getProductTotal().multiply(bd100).longValue());// 商品总额,单位分
		request.setUserNo(orderm.getStaffCode());// 操作人
		// 只匹配购买的商品或套餐，排除赠品
		List<OrderDetailIn> orderdetailins = orderin.getOrderDetailIns().stream()
				.filter(p -> Integer.compare(CommonConstant.GIFT_FLAG_0, p.getGiftFlag()) == 0)
				.collect(Collectors.toList());
		// 用,拼接商品编码
		String productCodes = orderdetailins.stream().map(OrderDetailIn::getProductCode)
				.collect(Collectors.joining(","));
		// 商品列表
		Map<String, ProductMainDTO> productMap = Optional
				.ofNullable(this.productClient.queryByProductCodes(productCodes).getData())
				.orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
		// 套餐列表
		Map<String, ProductMealListDTO> mealMap = Optional
				.ofNullable(this.mealClient.queryByIds(productCodes).getData()).orElse(Collections.emptyList()).stream()
				.collect(Collectors.toMap(ProductMealListDTO::getMealNo, Function.identity()));
		// 订单中的商品信息
		request.setOrderSubmitProductDtoList(orderdetailins.stream().map(m -> {
			OrderSubmitProductDto dto = new OrderSubmitProductDto();
			dto.setActivityBusNo(m.getActivityBusNo());// 活动业务/会员优惠业务主键
			dto.setActivityProductBusNo(m.getActivityProductBusNo());// 活动商品业务主键
			dto.setActivityTypeEnum(this.getActivityTypeEnum(m.getActivityType()));// 优惠途径
			dto.setDiscountId(m.getDiscountId());// 使用的优惠主键
			dto.setDiscountTypeEnum(this.getDiscountTypeEnum(m.getDiscountType()));// 优惠方式
			dto.setMemberCouponId(m.getMemberCouponId());// 使用的优惠券ID
			dto.setProductCode(m.getProductCode());// 商品code
			dto.setProductCount(m.getProductCount());// 商品数量
			if (Integer.compare(CommonConstant.MEAL_FLAG_0, m.getProductType()) == 0) {
				// 商品
				ProductMainDTO product = productMap.get(m.getProductCode());
				dto.setProductTotal(
						BigDecimal.valueOf(Double.valueOf(product.getSalePrice())).multiply(bd100).longValue());// 商品销售价,单位分
			} else {
				// 套餐
				ProductMealListDTO meal = mealMap.get(m.getProductCode());
				dto.setProductTotal(BigDecimal.valueOf(meal.getPriceD()).multiply(bd100).longValue());// 商品销售价,单位分
			}
			dto.setUseDiscountTypeEnum(this.getUseDiscountTypeEnum(m.getUseDiscountType()));// 使用的优惠
			return dto;
		}).collect(Collectors.toList()));
		return request;
	}

	/**
	 * 组装优惠使用记录数据
	 * 
	 * @param seq {@link AtomicInteger}
	 * @param in  {@link OrderDetailIn}
	 * @param od  {@link OrderDetail}
	 * @param pp  {@link ProductPriceResponse}
	 * @return {@link OrderCouponDetail}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午3:42:06
	 */
	private OrderCouponDetail getOrderCouponDetail2(AtomicInteger seq, OrderDetailIn in, OrderDetail od,
			ProductPriceResponse pp) {
		OrderCouponDetail cd = new OrderCouponDetail();
		// 优惠使用记录表业务主键
		cd.setOrderCouponDetailNo(String.format("%s%s", od.getOrderDetailCode(), seq.incrementAndGet()));
		cd.setOrderDetailCode(od.getOrderDetailCode());// 订单明细表业务标识
		cd.setOrderNo(od.getOrderNo());// 订单号
		cd.setProductCode(od.getProductCode());// 商品唯一标识
		cd.setActivityType(pp.getActivityType());// 优惠途径
		cd.setCouponCode(in.getDiscountId());// 优惠编号
		cd.setCouponType(pp.getDiscountType());// 优惠方式
		cd.setCouponMode(CommonConstant.COUPON_MODE_2);// 活动类型
		cd.setCouponAmt(pp.getActivityDiscountPrice().multiply(bd100).intValue());// 优惠金额 单位分
		cd.setCouponDirection(CommonConstant.COUPON_DIRECTION_1);// 优惠方向
		cd.setMealFlag(CommonConstant.MEAL_FLAG_0);// 是否套餐
		return cd;
	}

	/**
	 * 组装优惠使用记录数据
	 * 
	 * @param seq {@link AtomicInteger}
	 * @param in  {@link OrderDetailIn}
	 * @param od  {@link OrderDetail}
	 * @param pp  {@link ProductPriceResponse}
	 * @return {@link OrderCouponDetail}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午3:34:34
	 */
	private OrderCouponDetail getOrderCouponDetail1(AtomicInteger seq, OrderDetailIn in, OrderDetail od,
			ProductPriceResponse pp) {
		OrderCouponDetail cd = new OrderCouponDetail();
		// 优惠使用记录表业务主键
		cd.setOrderCouponDetailNo(String.format("%s%s", od.getOrderDetailCode(), seq.incrementAndGet()));
		cd.setOrderDetailCode(od.getOrderDetailCode());// 订单明细表业务标识
		cd.setOrderNo(od.getOrderNo());// 订单号
		cd.setProductCode(od.getProductCode());// 商品唯一标识
		cd.setActivityType(pp.getActivityType());// 优惠途径
		cd.setCouponCode(in.getDiscountId());// 优惠编号
		cd.setCouponType(pp.getDiscountType());// 优惠方式
		cd.setCouponMode(CommonConstant.COUPON_MODE_1);// 活动类型
		cd.setCouponAmt(pp.getCouponDiscountPrice().multiply(bd100).intValue());// 优惠金额 单位分
		cd.setCouponDirection(CommonConstant.COUPON_DIRECTION_1);// 优惠方向
		cd.setMealFlag(CommonConstant.MEAL_FLAG_0);// 是否套餐
		return cd;
	}

	/**
	 * 初始化订单
	 * 
	 * @param orderm {@link OrderM}
	 * @author zhangweiwei
	 * @date 2021年2月4日,上午10:29:31
	 */
	private void initOrder(OrderM orderm) {
		orderm.setTotal(0);// 实收金额=应收金额+预存
		orderm.setCash(0);// 应收金额=订单总额+邮费-优惠
		orderm.setTotalAll(0);// 订单总额
		orderm.setCash1(0);// 预存金额
		orderm.setSpend(0);// 消费金额=订单总额-优惠
		orderm.setPfee(0);// 邮费
		orderm.setPfeeFlag(CommonConstant.PFEE_FLAG_0);// 邮费承担方
		orderm.setAmountStored(0);// 使用储值金额 单位分
		orderm.setAmountRedEnvelope(0);// 使用红包金额 单位分
		orderm.setAmountCoupon(0);// 使用优惠券 单位分
		orderm.setPointsDeduction(0);// 使用积分抵扣 单位分
		orderm.setReturnAmountCoupon(0);
		orderm.setReturnAmountRedEnvelope(0);
		orderm.setReturnPointsDeduction(0);
		orderm.setOrderAfterSpare(0);
		orderm.setOrderAfterRed(0);
		orderm.setOrderAfterRebate(0);
		orderm.setOrderAfterIntegral(0);
		orderm.setRelationReissueOrderTotal(0);
		orderm.setLogisticsClaims(0);
		orderm.setInvoiceFlag(CommonConstant.INVOICE_0);// 不开票
		orderm.setRelationReissueOrderNo(CommonConstant.RELATION_REISSUE_ORDER_NO);
		orderm.setStaffCode(QueryIds.userNo.get());// 下单坐席编码
		orderm.setCreateTime(new Date());// 下单时间
		orderm.setOrderChanal(CommonConstant.ORDER_CHANAL_1);// 购物车下单
	}

	/**
	 * @param orderin {@link OrderIn}
	 * @return 是否使用账户金额
	 * @author zhangweiwei
	 * @date 2021年2月2日,下午7:15:59
	 */
	private boolean hasAmountStored(OrderIn orderin) {
		return orderin.getAmountStored() != null && orderin.getAmountStored().compareTo(BigDecimal.ZERO) > 0
				&& Integer.compare(PayType.PAY_TYPE_1.getCode(), orderin.getPayType()) == 0;
	}

	@PostMapping("/v1/updateorder")
	@ApiOperation(value = "订单列表-编辑", notes = "订单列表-编辑")
	public ComResponse<String> updateOrder(@RequestBody UpdateOrderIn orderin) {
		// 判断是否传入订单号
		if (!StringUtils.hasText(orderin.getOrderNo())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单号不能为空。");
		}
		// 判断是否传入订单明细
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按订单编号查询订单信息
		ComResponse<OrderM> oresponse = this.orderFeignClient.queryOrder(orderin.getOrderNo());
		// 如果调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(oresponse.getCode())) {
			log.error("订单列表-编辑>>调用查询订单[{}]信息接口失败", orderin.getOrderNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		OrderM orderm = oresponse.getData();
		// 如果不存在
		if (orderm == null) {
			log.error("订单列表-编辑>>调用查询订单[{}]信息接口失败", orderin.getOrderNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		orderm.setUpdateTime(new Date());// 修改时间
		orderm.setUpdateCode(QueryIds.userNo.get());// 修改人编码
		orderm.setCreateTime(orderm.getUpdateTime());
		orderm.setStaffCode(orderm.getUpdateCode());
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(orderm.getUpdateCode());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("订单列表-编辑>>{}", orderm.getUpdateCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用员工编号获取员工基本信息接口异常：%s", sresponse.getMessage()));
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("订单列表-编辑>>找不到该坐席[{}]信息", orderm.getUpdateCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6));// 使用redis重新生成订单号
		orderm.setUpdateName(staffInfo.getName());// 更新人姓名
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(String.valueOf(staffInfo.getDepartId()));// 下单坐席所属部门id
		// 按部门id查询部门信息
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(staffInfo.getDepartId());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
			log.error("订单列表-编辑>>{}", staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用id获取部门信息接口异常：%s", dresponse.getMessage()));
		}
		DepartDto depart = dresponse.getData();
		if (depart == null) {
			log.error("订单列表-编辑>>找不到该坐席[{}]所在部门[{}]的财务归属", orderm.getStaffCode(), staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席的财务归属。");
		}
		orderm.setFinancialOwner(depart.getFinanceDepartId());// 下单坐席财务归属部门id
		orderm.setFinancialOwnerName(depart.getFinanceDepartName());// 下单坐席财务归属部门名称
		// 按套餐和非套餐对订单明细进行分组，key为套餐标识，value为订单明细集合
		Map<Integer, List<OrderDetailIn>> orderdetailMap = orderin.getOrderDetailIns().stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getMealFlag));
		// 获取非套餐，也就是纯商品
		List<OrderDetailIn> orderProductList = orderdetailMap.get(CommonConstant.MEAL_FLAG_0);
		// 收集套餐关联的商品和非套餐商品
		List<OrderDetail> orderdetailList = new ArrayList<>();
		// 收集每类商品的库存，key为商品编码，value为商品库存
		Map<String, Integer> productStockMap = new HashMap<>();
		// 收集订单明细里商品的购买数量
		List<Tuple> tuples = new ArrayList<>();
		AtomicInteger seq = new AtomicInteger(10);// 循环序列
		orderm.setTotal(0);// 实收金额=应收金额+预存
		orderm.setCash(0);// 应收金额=订单总额+邮费-优惠
		orderm.setTotalAll(0);// 订单总额
		orderm.setSpend(0);// 消费金额=订单总额-优惠
		orderm.setCash1(0);// 预存金额
		orderm.setPfee(0);// 邮费
		// 如果有非套餐信息
		if (!CollectionUtils.isEmpty(orderProductList)) {
			// 收集商品编码
			List<String> productCodeList = orderProductList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接商品编码
			String productCodes = productCodeList.stream().collect(Collectors.joining(","));
			// 根据拼接后的商品编码查询商品列表
			ComResponse<List<ProductMainDTO>> presponse = this.productClient.queryByProductCodes(productCodes);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(presponse.getCode())) {
				log.error("订单列表-编辑>>{}", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用商品编码查询商品列表接口异常：%s", presponse.getMessage()));
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("订单列表-编辑>>找不到商品[{}]信息", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			if (Integer.compare(plist.size(), productCodeList.size()) != 0) {
				log.error("订单列表-编辑>>订单的商品编码总数[{}]与商品查询接口的商品编码总数[{}]不一致", productCodeList.size(), plist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的商品部分已下架。");
			}
			// 将商品编码作为key，商品对象作为value
			Map<String, ProductMainDTO> pmap = plist.stream()
					.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
			// 组装订单明细信息
			for (OrderDetailIn in : orderProductList) {
				ProductMainDTO p = pmap.get(in.getProductCode());
				// 如果商品已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, p.getStatus()) == 0) {
					log.error("订单列表-编辑>>该商品已下架>>{}", p);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品已下架。");
				}
				OrderDetail od = new OrderDetail();
				// 按主订单号生成订单明细编号
				od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
				od.setOrderNo(orderm.getOrderNo());// 订单编号
				od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
				od.setStaffCode(orderm.getUpdateCode());// 创建人编号
				od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
				od.setMemberName(orderm.getMemberName());// 顾客姓名
				od.setDepartId(orderm.getDepartId());// 部门表唯一标识
				od.setGiftFlag(CommonConstant.GIFT_FLAG_0);// 是否赠品
				od.setMealFlag(CommonConstant.MEAL_FLAG_0);// 不是套餐
				od.setProductCode(p.getProductCode());// 商品唯一标识
				od.setProductNo(p.getProductNo());// 商品编码
				od.setProductName(p.getName());// 商品名称
				od.setProductBarCode(p.getBarCode());// 产品条形码
				od.setProductUnitPrice(BigDecimal.valueOf(Double.valueOf(p.getSalePrice())).multiply(bd100).intValue());// 商品单价，单位分
				od.setProductCount(in.getProductCount());// 商品数量
				od.setUnit(p.getUnit());// 单位
				od.setSpec(String.valueOf(p.getTotalUseNum()));// 商品规格
				od.setPackageUnit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
				od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
				od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				orderdetailList.add(od);
			}
		}
		// 获取套餐
		List<OrderDetailIn> ordermealList = orderdetailMap.get(CommonConstant.MEAL_FLAG_1);
		// 如果有套餐信息
		if (!CollectionUtils.isEmpty(ordermealList)) {
			// 收集套餐编码
			List<String> mealNoList = ordermealList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接套餐编码
			String mealNos = mealNoList.stream().collect(Collectors.joining(","));
			// 根据拼接后的套餐编码查询套餐列表
			ComResponse<List<ProductMealListDTO>> mresponse = this.mealClient.queryByIds(mealNos);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresponse.getCode())) {
				log.error("订单列表-编辑>>{}", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用套餐编码查询套餐里商品列表接口异常：%s", mresponse.getMessage()));
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("订单列表-编辑>>找不到套餐[{}]信息", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			if (Integer.compare(mlist.size(), mealNoList.size()) != 0) {
				log.error("订单列表-编辑>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的套餐部分已下架。");
			}
			// 统计订单明细中的每个套餐的总数，key为套餐编码，value为购买套餐总数
			Map<String, Integer> mealCountMap = ordermealList.stream()
					.collect(Collectors.groupingBy(OrderDetailIn::getProductCode)).entrySet().stream()
					.collect(Collectors.toMap(Entry::getKey,
							v -> v.getValue().stream().mapToInt(OrderDetailIn::getProductCount).sum()));
			for (ProductMealListDTO meal : mlist) {
				// 如果套餐已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, meal.getStatus()) == 0) {
					log.error("订单列表-编辑>>该套餐已下架>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐已下架。");
				}
				// 如果套餐里没有商品
				if (CollectionUtils.isEmpty(meal.getMealProductList())) {
					log.error("订单列表-编辑>>该套餐没有包含商品信息>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐没有包含商品信息。");
				}
				// 套餐数量
				Integer mealCount = mealCountMap.get(meal.getMealNo());
				// 套餐价，单位分
				BigDecimal mealPrice = BigDecimal.valueOf(meal.getPriceD()).multiply(bd100);
				// 组装订单明细信息
				List<OrderDetail> result = meal.getMealProductList().stream().map(in -> {
					OrderDetail od = new OrderDetail();
					// 按主订单号生成订单明细编号
					od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
					od.setOrderNo(orderm.getOrderNo());// 订单编号
					od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
					od.setStaffCode(orderm.getUpdateCode());// 创建人编号
					od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
					od.setMemberName(orderm.getMemberName());// 顾客姓名
					od.setDepartId(orderm.getDepartId());// 部门表唯一标识
					od.setGiftFlag(in.getMealGiftFlag());// 是否赠品
					od.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是套餐
					od.setMealName(meal.getName());// 套餐名称
					od.setMealNo(meal.getMealNo());// 套餐唯一标识
					od.setMealCount(mealCount);// 套餐数量
					od.setMealPrice(mealPrice.intValue());// 套餐价格，单位分
					od.setProductCode(in.getProductCode());// 商品唯一标识
					od.setProductNo(in.getProductNo());// 商品编码
					od.setProductName(in.getName());// 商品名称
					od.setProductBarCode(in.getBarCode());// 产品条形码
					od.setProductUnitPrice(in.getSalePrice());// 商品单价，单位分
					od.setProductCount(in.getProductNum() * mealCount);// 商品数量*套餐数量
					od.setUnit(in.getUnit());// 单位
					od.setSpec(String.valueOf(in.getTotalUseNum()));// 商品规格
					od.setPackageUnit(in.getPackagingUnit());// 包装单位
					productStockMap.put(od.getProductCode(), in.getStock());// 库存
					tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList());
				// 套餐商品总金额
				BigDecimal orderdetailTotal = BigDecimal.valueOf(result.stream().mapToInt(OrderDetail::getTotal).sum());
				orderdetailList.addAll(result.stream().map(od -> {
					BigDecimal price = mealPrice.multiply(BigDecimal.valueOf(od.getProductUnitPrice()))
							.multiply(BigDecimal.valueOf(mealCount))
							.divide(orderdetailTotal, 0, BigDecimal.ROUND_HALF_UP);
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setProductUnitPrice(price.intValue());
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setProductUnitPrice(0);
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList()));
			}
		}
		orderm.setTotal(orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
		orderm.setCash(orderdetailList.stream().mapToInt(OrderDetail::getCash).sum());
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		// 如果支付形式是客户账户扣款
		if (Integer.compare(PayMode.PAY_MODE_4.getCode(), orderm.getPayMode()) == 0) {
			// 按顾客号查询顾客账号
			ComResponse<MemberAmountDto> maresponse = this.memberFien.getMemberAmount(orderm.getMemberCardNo());
			// 如果调用服务异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(maresponse.getCode())) {
				log.error("订单列表-编辑>>{}", orderm.getMemberCardNo());
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用查询顾客账号接口异常：%s", maresponse.getMessage()));
			}
			MemberAmountDto account = maresponse.getData();
			if (account == null) {
				log.error("订单列表-编辑>>找不到该顾客[{}]账号", orderm.getMemberCardNo());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
			}
			// 如果订单总金额大于账户剩余金额，单位分
			if (orderm.getTotal() > account.getTotalMoney()) {
				log.error("订单列表-编辑>>订单[{}]总金额[{}]大于账户剩余金额[{}]", orderm.getOrderNo(), orderm.getTotal(),
						account.getTotalMoney());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "账户余额不足。");
			}
		}
		// 统计订单明细中的每类商品的总数，key为商品编码，value为购买商品总数
		Map<String, Integer> productCountMap = new HashMap<>();
		tuples.stream().forEach(p -> productCountMap.merge(p.get(0), p.get(1), Integer::sum));
		// 校验订单购买商品的总数是否超出库存数
		Set<Entry<String, Integer>> entrySet = productCountMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			Integer pstock = productStockMap.get(entry.getKey());// 取出商品库存
			// 如果购买商品总数大于商品库存
			if (entry.getValue() > pstock) {
				log.error("订单列表-编辑>>该订单[{}]商品[{}]购买总数[{}]大于库存总数[{}]", orderm.getOrderNo(), entry.getKey(),
						entry.getValue(), pstock);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品库存不足。");
			}
		}
		// 组装扣减库存参数
		OrderProductVO orderProduct = new OrderProductVO();
		orderProduct.setOrderNo(orderm.getOrderNo());// 订单编号
		orderProduct.setProductReduceVOS(entrySet.stream().map(m -> {
			ProductReduceVO vo = new ProductReduceVO();
			vo.setNum(m.getValue());// 商品数量
			vo.setProductCode(m.getKey());// 商品编号
			vo.setOrderNo(orderm.getOrderNo());// 订单编号
			return vo;
		}).collect(Collectors.toList()));
		// 调用扣减库存服务接口
		ComResponse<?> productReduce = this.productClient.productReduce(orderProduct);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(productReduce.getCode())) {
			log.error("订单列表-编辑>>{}", this.toJsonString(orderProduct));
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用扣减库存接口异常：%s", productReduce.getMessage()));
		}
		// 如果支付形式是客户账户扣款
		if (Integer.compare(PayMode.PAY_MODE_4.getCode(), orderm.getPayMode()) == 0) {
			// 组装顾客账户消费参数
			MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
			memberAmountDetail.setDiscountMoney(orderm.getTotal());// 订单总金额，单位分
			memberAmountDetail.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
			memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_2);// 消费
			memberAmountDetail.setOrderNo(orderm.getOrderNo());// 订单编号
			memberAmountDetail.setRemark("订单列表-编辑:修改订单");// 备注
			// 调用顾客账户消费服务接口
			ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
				log.error("订单列表-编辑>>{}", customerAmountOperation);
				// 恢复库存
				ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
				// 如果调用服务接口失败
				if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
					log.error("订单列表-编辑>>{}", increaseStock);
					this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL,
							ProductClient.INCREASE_STOCK_URL, orderm.getStaffCode(), orderm.getOrderNo());
				}
				return ComResponse.fail(ResponseCodeEnums.ERROR, customerAmountOperation.getMessage());
			}
		}
		log.info("订单: {}", this.toJsonString(orderm));
		log.info("订单明细: {}", this.toJsonString(orderdetailList));
		// 调用修改订单服务接口
		ComResponse<?> updateOrder = this.orderFeignClient.updateOrder(new OrderRequest(orderm, orderdetailList));
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(updateOrder.getCode())) {
			log.error("订单列表-编辑>>修改订单失败[订单号：{}]>>{}", orderm.getOrderNo(), updateOrder);
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("订单列表-编辑>>{}", increaseStock);
				this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL, ProductClient.INCREASE_STOCK_URL,
						orderm.getStaffCode(), orderm.getOrderNo());
			}
			// 如果支付形式是客户账户扣款
			if (Integer.compare(PayMode.PAY_MODE_4.getCode(), orderm.getPayMode()) == 0) {
				// 组装顾客恢复账户参数
				MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
				memberAmountDetail.setDiscountMoney(orderm.getTotal());// 订单总金额，单位分
				memberAmountDetail.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
				memberAmountDetail.setOrderNo(orderm.getOrderNo());// 订单编号
				memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 退回
				memberAmountDetail.setRemark("订单列表-编辑:退回金额");// 备注
				ComResponse<?> customerAmountOperation2 = this.memberFien.customerAmountOperation(memberAmountDetail);
				// 如果调用服务接口失败
				if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation2.getCode())) {
					log.error("订单列表-编辑>>{}", customerAmountOperation2);
					this.orderCommonService.insert(memberAmountDetail, MemberFien.SUFFIX_URL,
							MemberFien.CUSTOMER_AMOUNT_OPERATION_URL, orderm.getStaffCode(), orderm.getOrderNo());
				}
				return ComResponse.fail(ResponseCodeEnums.ERROR, "修改订单失败，请稍后重试。");
			}
		}
		// 顾客管理-处理下单时更新顾客信息
		OrderCreateInfoVO orderCreateInfoVO = new OrderCreateInfoVO();
		orderCreateInfoVO.setCreateTime(orderm.getCreateTime());// 下单时间
		orderCreateInfoVO.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
		orderCreateInfoVO.setOrderNo(orderm.getOrderNo());// 订单编号
		orderCreateInfoVO.setStaffNo(orderm.getStaffCode());// 下单坐席编号
		ComResponse<Boolean> createUpdateMember = this.memberFien.dealOrderCreateUpdateMemberData(orderCreateInfoVO);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(createUpdateMember.getCode())) {
			log.error("订单列表-编辑>>{}", createUpdateMember);
			this.orderCommonService.insert(createUpdateMember, MemberFien.SUFFIX_URL,
					MemberFien.DEAL_ORDER_CREATE_UPDATE_MEMBER_DATA_URL, orderm.getStaffCode(), orderm.getOrderNo());
		}
		log.info("订单列表-编辑>>修改订单成功[订单号：{}]", orderm.getOrderNo());
		return ComResponse.success(orderm.getOrderNo());
	}

	@PostMapping("/v1/reissueorder")
	@ApiOperation(value = "订单列表-异常处理-补发订单", notes = "订单列表-异常处理-补发订单")
	public ComResponse<Object> reissueOrder(@RequestBody ReissueOrderIn orderin) {
		// 判断是否传入订单号
		if (!StringUtils.hasText(orderin.getOrderNo())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单号不能为空。");
		}
		// 判断是否传入订单明细
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按订单编号查询订单信息
		ComResponse<OrderM> oresponse = this.orderFeignClient.queryOrder(orderin.getOrderNo());
		// 如果调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(oresponse.getCode())) {
			log.error("订单列表-异常处理-补发订单>>调用查询订单[{}]信息接口失败", orderin.getOrderNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		OrderM orderm = oresponse.getData();
		// 如果不存在
		if (orderm == null) {
			log.error("订单列表-异常处理-补发订单>>调用查询订单[{}]信息接口失败", orderin.getOrderNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		// 如果存在补发订单，则不允许再次对该订单进行补发操作
		if (!CommonConstant.RELATION_REISSUE_ORDER_NO.equals(orderm.getRelationReissueOrderNo())) {
			log.error("订单列表-异常处理-补发订单>>该订单[{}]已存在补发订单[{}]", orderin.getOrderNo(), orderm.getRelationReissueOrderNo());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单已存在补发订单。");
		}
		orderm.setUpdateTime(new Date());// 修改时间
		orderm.setUpdateCode(QueryIds.userNo.get());// 修改人编码
		orderm.setCreateTime(orderm.getUpdateTime());
		orderm.setStaffCode(orderm.getUpdateCode());
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(orderm.getUpdateCode());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("订单列表-异常处理-补发订单>>{}", orderm.getUpdateCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用员工编号获取员工基本信息接口异常：%s", sresponse.getMessage()));
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("订单列表-异常处理-补发订单>>找不到该坐席[{}]信息", orderm.getUpdateCode());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6));// 使用redis重新生成订单号
		orderm.setUpdateName(staffInfo.getName());// 更新人姓名
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(String.valueOf(staffInfo.getDepartId()));// 下单坐席所属部门id
		// 按部门id查询部门信息
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(staffInfo.getDepartId());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
			log.error("订单列表-异常处理-补发订单>>{}", staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用id获取部门信息接口异常：%s", dresponse.getMessage()));
		}
		DepartDto depart = dresponse.getData();
		if (depart == null) {
			log.error("订单列表-异常处理-补发订单>>找不到该坐席[{}]所在部门[{}]的财务归属", orderm.getStaffCode(), staffInfo.getDepartId());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席的财务归属。");
		}
		orderm.setFinancialOwner(depart.getFinanceDepartId());// 下单坐席财务归属部门id
		orderm.setFinancialOwnerName(depart.getFinanceDepartName());// 下单坐席财务归属部门名称
		// 按套餐和非套餐对订单明细进行分组，key为套餐标识，value为订单明细集合
		Map<Integer, List<OrderDetailIn>> orderdetailMap = orderin.getOrderDetailIns().stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getMealFlag));
		// 获取非套餐，也就是纯商品
		List<OrderDetailIn> orderProductList = orderdetailMap.get(CommonConstant.MEAL_FLAG_0);
		// 收集套餐关联的商品和非套餐商品
		List<OrderDetail> orderdetailList = new ArrayList<>();
		// 收集每类商品的库存，key为商品编码，value为商品库存
		Map<String, Integer> productStockMap = new HashMap<>();
		// 收集订单明细里商品的购买数量
		List<Tuple> tuples = new ArrayList<>();
		AtomicInteger seq = new AtomicInteger(10);// 循环序列
		orderm.setTotal(0);// 实收金额=应收金额+预存
		orderm.setCash(0);// 应收金额=订单总额+邮费-优惠
		orderm.setTotalAll(0);// 订单总额
		orderm.setSpend(0);// 消费金额=订单总额-优惠
		orderm.setCash1(0);// 预存金额
		orderm.setPfee(0);// 邮费
		// 如果有非套餐信息
		if (!CollectionUtils.isEmpty(orderProductList)) {
			// 收集商品编码
			List<String> productCodeList = orderProductList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接商品编码
			String productCodes = productCodeList.stream().collect(Collectors.joining(","));
			// 根据拼接后的商品编码查询商品列表
			ComResponse<List<ProductMainDTO>> presponse = this.productClient.queryByProductCodes(productCodes);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(presponse.getCode())) {
				log.error("订单列表-异常处理-补发订单>>{}", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用商品编码查询商品列表接口异常：%s", presponse.getMessage()));
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("订单列表-异常处理-补发订单>>找不到商品[{}]信息", productCodes);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			if (Integer.compare(plist.size(), productCodeList.size()) != 0) {
				log.error("订单列表-异常处理-补发订单>>订单的商品编码总数[{}]与商品查询接口的商品编码总数[{}]不一致", productCodeList.size(), plist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的商品部分已下架。");
			}
			// 将商品编码作为key，商品对象作为value
			Map<String, ProductMainDTO> pmap = plist.stream()
					.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
			// 组装订单明细信息
			for (OrderDetailIn in : orderProductList) {
				ProductMainDTO p = pmap.get(in.getProductCode());
				// 如果商品已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, p.getStatus()) == 0) {
					log.error("订单列表-异常处理-补发订单>>该商品已下架>>{}", p);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品已下架。");
				}
				OrderDetail od = new OrderDetail();
				// 按主订单号生成订单明细编号
				od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
				od.setOrderNo(orderm.getOrderNo());// 订单编号
				od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
				od.setStaffCode(orderm.getUpdateCode());// 创建人编号
				od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
				od.setMemberName(orderm.getMemberName());// 顾客姓名
				od.setDepartId(orderm.getDepartId());// 部门表唯一标识
				od.setGiftFlag(CommonConstant.GIFT_FLAG_0);// 是否赠品
				od.setMealFlag(CommonConstant.MEAL_FLAG_0);// 不是套餐
				od.setProductCode(p.getProductCode());// 商品唯一标识
				od.setProductNo(p.getProductNo());// 商品编码
				od.setProductName(p.getName());// 商品名称
				od.setProductBarCode(p.getBarCode());// 产品条形码
				od.setProductUnitPrice(BigDecimal.valueOf(Double.valueOf(p.getSalePrice())).multiply(bd100).intValue());// 商品单价，单位分
				od.setProductCount(in.getProductCount());// 商品数量
				od.setUnit(p.getUnit());// 单位
				od.setSpec(String.valueOf(p.getTotalUseNum()));// 商品规格
				od.setPackageUnit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
				od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
				od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				orderdetailList.add(od);
			}
		}
		// 获取套餐
		List<OrderDetailIn> ordermealList = orderdetailMap.get(CommonConstant.MEAL_FLAG_1);
		// 如果有套餐信息
		if (!CollectionUtils.isEmpty(ordermealList)) {
			// 收集套餐编码
			List<String> mealNoList = ordermealList.stream().map(OrderDetailIn::getProductCode).distinct()
					.collect(Collectors.toList());
			// 用,拼接套餐编码
			String mealNos = mealNoList.stream().collect(Collectors.joining(","));
			// 根据拼接后的套餐编码查询套餐列表
			ComResponse<List<ProductMealListDTO>> mresponse = this.mealClient.queryByIds(mealNos);
			// 如果服务调用异常
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresponse.getCode())) {
				log.error("订单列表-异常处理-补发订单>>{}", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR,
						String.format("调用套餐编码查询套餐里商品列表接口异常：%s", mresponse.getMessage()));
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("订单列表-异常处理-补发订单>>找不到套餐[{}]信息", mealNos);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			if (Integer.compare(mlist.size(), mealNoList.size()) != 0) {
				log.error("订单列表-异常处理-补发订单>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的套餐部分已下架。");
			}
			// 统计订单明细中的每个套餐的总数，key为套餐编码，value为购买套餐总数
			Map<String, Integer> mealCountMap = ordermealList.stream()
					.collect(Collectors.groupingBy(OrderDetailIn::getProductCode)).entrySet().stream()
					.collect(Collectors.toMap(Entry::getKey,
							v -> v.getValue().stream().mapToInt(OrderDetailIn::getProductCount).sum()));
			for (ProductMealListDTO meal : mlist) {
				// 如果套餐已下架
				if (Integer.compare(CommonConstant.PRODUCT_AND_MEAL_STATUS_0, meal.getStatus()) == 0) {
					log.error("订单列表-异常处理-补发订单>>该套餐已下架>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐已下架。");
				}
				// 如果套餐里没有商品
				if (CollectionUtils.isEmpty(meal.getMealProductList())) {
					log.error("订单列表-异常处理-补发订单>>该套餐没有包含商品信息>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐没有包含商品信息。");
				}
				// 套餐数量
				Integer mealCount = mealCountMap.get(meal.getMealNo());
				// 套餐价，单位分
				BigDecimal mealPrice = BigDecimal.valueOf(meal.getPriceD()).multiply(bd100);
				// 组装订单明细信息
				List<OrderDetail> result = meal.getMealProductList().stream().map(in -> {
					OrderDetail od = new OrderDetail();
					// 按主订单号生成订单明细编号
					od.setOrderDetailCode(String.format("%s%s", orderm.getOrderNo(), seq.incrementAndGet()));
					od.setOrderNo(orderm.getOrderNo());// 订单编号
					od.setUpdateCode(orderm.getUpdateCode());// 更新人编号
					od.setStaffCode(orderm.getUpdateCode());// 创建人编号
					od.setMemberCardNo(orderm.getMemberCardNo());// 顾客卡号
					od.setMemberName(orderm.getMemberName());// 顾客姓名
					od.setDepartId(orderm.getDepartId());// 部门表唯一标识
					od.setGiftFlag(in.getMealGiftFlag());// 是否赠品
					od.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是套餐
					od.setMealName(meal.getName());// 套餐名称
					od.setMealNo(meal.getMealNo());// 套餐唯一标识
					od.setMealCount(mealCount);// 套餐数量
					od.setMealPrice(mealPrice.intValue());// 套餐价格，单位分
					od.setProductCode(in.getProductCode());// 商品唯一标识
					od.setProductNo(in.getProductNo());// 商品编码
					od.setProductName(in.getName());// 商品名称
					od.setProductBarCode(in.getBarCode());// 产品条形码
					od.setProductUnitPrice(in.getSalePrice());// 商品单价，单位分
					od.setProductCount(in.getProductNum() * mealCount);// 商品数量*套餐数量
					od.setUnit(in.getUnit());// 单位
					od.setSpec(String.valueOf(in.getTotalUseNum()));// 商品规格
					od.setPackageUnit(in.getPackagingUnit());// 包装单位
					productStockMap.put(od.getProductCode(), in.getStock());// 库存
					tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList());
				// 套餐商品总金额
				BigDecimal orderdetailTotal = BigDecimal.valueOf(result.stream().mapToInt(OrderDetail::getTotal).sum());
				orderdetailList.addAll(result.stream().map(od -> {
					BigDecimal price = mealPrice.multiply(BigDecimal.valueOf(od.getProductUnitPrice()))
							.multiply(BigDecimal.valueOf(mealCount))
							.divide(orderdetailTotal, 0, BigDecimal.ROUND_HALF_UP);
					// 如果是非赠品
					if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
						od.setProductUnitPrice(price.intValue());
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setProductUnitPrice(0);
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList()));
			}
		}
		orderm.setTotal(orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
		orderm.setCash(orderdetailList.stream().mapToInt(OrderDetail::getCash).sum());
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		// 统计订单明细中的每类商品的总数，key为商品编码，value为购买商品总数
		Map<String, Integer> productCountMap = new HashMap<>();
		tuples.stream().forEach(p -> productCountMap.merge(p.get(0), p.get(1), Integer::sum));
		// 校验订单购买商品的总数是否超出库存数
		Set<Entry<String, Integer>> entrySet = productCountMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			Integer pstock = productStockMap.get(entry.getKey());// 取出商品库存
			// 如果购买商品总数大于商品库存
			if (entry.getValue() > pstock) {
				log.error("订单列表-异常处理-补发订单>>该订单[{}]商品[{}]购买总数[{}]大于库存总数[{}]", orderm.getOrderNo(), entry.getKey(),
						entry.getValue(), pstock);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品库存不足。");
			}
		}
		// 组装扣减库存参数
		OrderProductVO orderProduct = new OrderProductVO();
		orderProduct.setOrderNo(orderm.getOrderNo());// 订单编号
		orderProduct.setProductReduceVOS(entrySet.stream().map(m -> {
			ProductReduceVO vo = new ProductReduceVO();
			vo.setNum(m.getValue());// 商品数量
			vo.setProductCode(m.getKey());// 商品编号
			vo.setOrderNo(orderm.getOrderNo());// 订单编号
			return vo;
		}).collect(Collectors.toList()));
		// 调用扣减库存服务接口
		ComResponse<?> productReduce = this.productClient.productReduce(orderProduct);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(productReduce.getCode())) {
			log.error("订单列表-异常处理-补发订单>>{}", this.toJsonString(orderProduct));
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用扣减库存接口异常：%s", productReduce.getMessage()));
		}
		log.info("订单: {}", this.toJsonString(orderm));
		log.info("订单明细: {}", this.toJsonString(orderdetailList));
		// 调用补发订单服务接口
		ComResponse<Object> reissueOrder = this.orderFeignClient.reissueOrder(new OrderRequest(orderm, orderdetailList,
				orderin.getOrderNo(), orderin.getPayAmount(), orderin.getRemark()));
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(reissueOrder.getCode())) {
			log.error("订单列表-异常处理-补发订单>>创建订单失败[订单号：{}]", orderm.getOrderNo());
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("订单列表-异常处理-补发订单>>{}", increaseStock);
				this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL, ProductClient.INCREASE_STOCK_URL,
						orderm.getStaffCode(), orderm.getOrderNo());
			}
		}
		// 组装物流赔付参数
		ExpressIndemnity indemnity = new ExpressIndemnity();
		indemnity.setCharge(orderin.getPayAmount());
		indemnity.setExpressNum(orderm.getExpressNumber());
		// 调用物流赔付接口
		ComResponse<Boolean> settlementLogisticsChargeIndemnity = this.logisticsFien
				.settlementLogisticsChargeIndemnity(indemnity);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(settlementLogisticsChargeIndemnity.getCode())) {
			log.error("订单列表-异常处理-补发订单>>{}", this.toJsonString(indemnity));
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用物流赔付接口异常：%s", settlementLogisticsChargeIndemnity.getMessage()));
		}
		// 顾客管理-处理下单时更新顾客信息
		OrderCreateInfoVO orderCreateInfoVO = new OrderCreateInfoVO();
		orderCreateInfoVO.setCreateTime(orderm.getCreateTime());// 下单时间
		orderCreateInfoVO.setMemberCard(orderm.getMemberCardNo());// 顾客卡号
		orderCreateInfoVO.setOrderNo(orderm.getOrderNo());// 订单编号
		orderCreateInfoVO.setStaffNo(orderm.getStaffCode());// 下单坐席编号
		ComResponse<Boolean> createUpdateMember = this.memberFien.dealOrderCreateUpdateMemberData(orderCreateInfoVO);
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(createUpdateMember.getCode())) {
			log.error("订单列表-异常处理-补发订单>>{}", createUpdateMember);
			this.orderCommonService.insert(createUpdateMember, MemberFien.SUFFIX_URL,
					MemberFien.DEAL_ORDER_CREATE_UPDATE_MEMBER_DATA_URL, orderm.getStaffCode(), orderm.getOrderNo());
		}
		log.info("订单列表-异常处理-补发订单>>创建订单成功[订单号：{}]", orderm.getOrderNo());
		return ComResponse.success(orderm.getOrderNo());
	}

	@GetMapping("/v1/leaderboard")
	@ApiOperation(value = "业绩排行榜", notes = "业绩排行榜")
	public ComResponse<List<LeaderBoard>> queryLeaderboard(
			@ApiParam("今日/3日/7日") @RequestParam LeaderBoardType boardType,
			@ApiParam("1：热线，2：回访") @RequestParam int workOrderType) {
		List<LeaderBoard> data = this.orderFeignClient.queryLeaderboard(boardType, workOrderType).getData();
		if (!CollectionUtils.isEmpty(data)) {
			List<String> staffCodes = data.stream().map(LeaderBoard::getStaffCode).collect(Collectors.toList());
			List<StaffDetail> details = this.ehrStaffClient.getDetailsListByNo(staffCodes).getData();
			if (!CollectionUtils.isEmpty(details)) {
				Map<String, StaffDetail> sdMap = details.stream()
						.collect(Collectors.toMap(StaffDetail::getStaffNo, Function.identity()));
				return ComResponse.success(data.stream().map(m -> {
					StaffDetail sd = sdMap.get(m.getStaffCode());
					m.setStaffName(sd.getName());
					m.setDepartName(sd.getDepartName());
					return m;
				}).collect(Collectors.toList()));
			}
		}
		return ComResponse.success(data);
	}

	@GetMapping("/v1/memberfirstorderchannel/{memberCard}")
	@ApiOperation(value = "查询顾客首单渠道", notes = "查询顾客首单渠道")
	public ComResponse<MemberChannel> queryMemberFirstOrderChannel(
			@ApiParam(value = "顾客卡号", required = true) @PathVariable String memberCard) {
		return this.orderFeignClient.queryMemberFirstOrderChannel(memberCard);
	}

	@Resource
	private OrderFeignClient orderFeignClient;
	@Resource
	private MealClient mealClient;
	@Resource
	private ProductClient productClient;
	@Resource
	private MemberFien memberFien;
	@Resource
	private EhrStaffClient ehrStaffClient;
	@Resource
	private IOrderCommonService orderCommonService;
	@Resource
	private ActivityClient activityClient;
	@Resource
	private LogisticsFien logisticsFien;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private ObjectMapper objectMapper;
}

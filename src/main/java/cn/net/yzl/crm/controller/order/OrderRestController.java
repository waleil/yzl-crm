package cn.net.yzl.crm.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.lang.Tuple;
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
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.order.CalcOrderIn;
import cn.net.yzl.crm.model.order.CalcOrderOut;
import cn.net.yzl.crm.model.order.OrderOut;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.IOrderCommonService;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.PayType;
import cn.net.yzl.order.enums.RedisKeys;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
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
	@PostMapping("/v1/calcorder")
	@ApiOperation(value = "热线工单-购物车-计算订单金额", notes = "热线工单-购物车-计算订单金额")
	public ComResponse<CalcOrderOut> calcOrder(@RequestBody CalcOrderIn orderin) {
		// 按套餐和非套餐对订单明细进行分组，key为套餐标识，value为订单明细集合
		Map<Integer, List<OrderDetailIn>> orderdetailMap = orderin.getOrderDetailIns().stream()
				.collect(Collectors.groupingBy(OrderDetailIn::getMealFlag));
		// 获取非套餐，也就是纯商品
		List<OrderDetailIn> orderProductList = orderdetailMap.get(CommonConstant.MEAL_FLAG_0);
		// 收集套餐关联的商品和非套餐商品
		List<OrderDetail> orderdetailList = new ArrayList<>();
		BigDecimal bd100 = BigDecimal.valueOf(100);// 元转分
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
				log.error("热线工单-购物车-计算订单金额>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("热线工单-购物车-计算订单金额>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			if (Integer.compare(plist.size(), productCodeList.size()) != 0) {
				log.error("热线工单-购物车-计算订单金额>>订单的商品编码总数[{}]与商品查询接口的商品编码总数[{}]不一致", productCodeList.size(), plist.size());
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
					log.error("热线工单-购物车-计算订单金额>>该商品[{}]已下架>>{}", p);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品已下架。");
				}
				OrderDetail od = new OrderDetail();
				// 按主订单号生成订单明细编号
				od.setGiftFlag(in.getGiftFlag());// 是否赠品
				od.setMealFlag(CommonConstant.MEAL_FLAG_0);// 不是套餐
				od.setProductCode(p.getProductCode());// 商品唯一标识
				od.setProductNo(p.getProductNo());// 商品编码
				od.setProductUnitPrice(BigDecimal.valueOf(Double.valueOf(p.getSalePrice())).multiply(bd100).intValue());// 商品单价，单位分
				od.setProductCount(in.getProductCount());// 商品数量
				// 如果是非赠品
				if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
					od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
					od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				} else {// 如果是赠品，将金额设置为0
					od.setTotal(0);// 实收金额，单位分
					od.setCash(0);// 应收金额，单位分
				}
				orderdetailList.add(od);
			}
			orderin.setTotal(orderin.getTotal() + orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
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
				log.error("热线工单-购物车-计算订单金额>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("热线工单-购物车-计算订单金额>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			if (Integer.compare(mlist.size(), mealNoList.size()) != 0) {
				log.error("热线工单-购物车-计算订单金额>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
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
					log.error("热线工单-购物车-计算订单金额>>该套餐已下架>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐已下架。");
				}
				// 如果套餐里没有商品
				if (CollectionUtils.isEmpty(meal.getMealProductList())) {
					log.error("热线工单-购物车-计算订单金额>>该套餐没有包含商品信息>>{}", meal);
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
					od.setGiftFlag(in.getMealGiftFlag());// 是否赠品
					od.setMealFlag(CommonConstant.MEAL_FLAG_1);// 是套餐
					od.setMealNo(meal.getMealNo());// 套餐唯一标识
					od.setMealCount(mealCount);// 套餐数量
					od.setMealPrice(mealPrice.intValue());// 套餐价格，单位分
					od.setProductCode(in.getProductCode());// 商品唯一标识
					od.setProductNo(in.getProductNo());// 商品编码
					od.setProductUnitPrice(in.getSalePrice());// 商品单价，单位分
					od.setProductCount(in.getProductNum() * mealCount);// 商品数量*套餐数量
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
			orderin.setTotal(orderin.getTotal()
					+ mlist.stream().mapToInt(m -> BigDecimal.valueOf(m.getPriceD()).multiply(bd100).intValue()).sum());
		}
		return ComResponse.success(new CalcOrderOut(BigDecimal.valueOf(orderin.getTotal()).divide(bd100).doubleValue(),
				BigDecimal.valueOf(orderin.getTotal()).divide(bd100).doubleValue(), 0d, 0d, 0d));
	}

	@PostMapping("/v1/submitorder")
	@ApiOperation(value = "热线工单-购物车-提交订单", notes = "热线工单-购物车-提交订单")
	public ComResponse<OrderOut> submitOrder(@RequestBody OrderIn orderin) {
		OrderM orderm = new OrderM();// 订单信息
		this.initOrder(orderm);
		// 如果订单里没有商品
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			log.error("热线工单-购物车-提交订单>>订单明细集合里没有任何元素>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按顾客号查询顾客信息
		GeneralResult<Member> mresult = this.memberFien.getMember(orderin.getMemberCardNo());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresult.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]信息>>{}", orderin.getMemberCardNo(), mresult);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客信息。");
		}
		Member member = mresult.getData();
		if (member == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]信息>>{}", orderin.getMemberCardNo(), member);
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
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]收货地址>>{}", orderin.getMemberCardNo(), raresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客收货地址。");
		}
		List<ReveiverAddressDto> reveiverAddresses = raresponse.getData();
		if (CollectionUtils.isEmpty(reveiverAddresses)) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]收货地址>>{}", orderin.getMemberCardNo(), raresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客收货地址。");
		}
		ReveiverAddressDto reveiverAddress = reveiverAddresses.stream()
				.filter(p -> p.getId().equals(orderin.getReveiverAddressNo())).findFirst().orElse(null);
		if (reveiverAddress == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]收货地址>>{}", orderin.getMemberCardNo(), reveiverAddress);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客收货地址。");
		}
		// 按顾客号查询顾客账号
		ComResponse<MemberAmountDto> maresponse = this.memberFien.getMemberAmount(orderin.getMemberCardNo());
		// 如果调用服务异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(maresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]账号>>{}", orderin.getMemberCardNo(), maresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
		}
		MemberAmountDto account = maresponse.getData();
		if (account == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客[{}]账号>>{}", orderin.getMemberCardNo(), account);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
		}
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(orderm.getStaffCode());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]信息>>{}", orderm.getStaffCode(), sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]信息>>{}", orderm.getStaffCode(), sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6));// 使用redis生成订单号
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(String.valueOf(staffInfo.getDepartId()));// 下单坐席所属部门id
		orderm.setUpdateCode(orderm.getStaffCode());// 更新人编号
		orderm.setUpdateName(staffInfo.getName());// 更新人姓名
		// 按部门id查询部门信息
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(staffInfo.getDepartId());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]所在部门[{}]的财务归属>>{}", orderm.getStaffCode(), staffInfo.getDepartId(),
					dresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席的财务归属。");
		}
		DepartDto depart = dresponse.getData();
		if (depart == null) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席[{}]所在部门[{}]的财务归属>>{}", orderm.getStaffCode(), staffInfo.getDepartId(),
					dresponse);
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
		BigDecimal bd100 = BigDecimal.valueOf(100);// 元转分
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
				log.error("热线工单-购物车-提交订单>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("热线工单-购物车-提交订单>>找不到商品[{}]信息>>{}", productCodes, presponse);
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
					log.error("热线工单-购物车-提交订单>>该商品[{}]已下架>>{}", p);
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
				od.setPackageunit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
				// 如果是非赠品
				if (Integer.compare(CommonConstant.GIFT_FLAG_0, od.getGiftFlag()) == 0) {
					od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
					od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				} else {// 如果是赠品，将金额设置为0
					od.setTotal(0);// 实收金额，单位分
					od.setCash(0);// 应收金额，单位分
				}
				orderdetailList.add(od);
			}
			orderm.setTotal(orderm.getTotal() + orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
			orderm.setCash(orderm.getCash() + orderdetailList.stream().mapToInt(OrderDetail::getCash).sum());
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
				log.error("热线工单-购物车-提交订单>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("热线工单-购物车-提交订单>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			if (Integer.compare(mlist.size(), mealNoList.size()) != 0) {
				log.error("热线工单-购物车-提交订单>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
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
					od.setPackageunit(in.getPackagingUnit());// 包装单位
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
			orderm.setTotal(orderm.getTotal()
					+ mlist.stream().mapToInt(m -> BigDecimal.valueOf(m.getPriceD()).multiply(bd100).intValue()).sum());
			orderm.setCash(orderm.getCash()
					+ mlist.stream().mapToInt(m -> BigDecimal.valueOf(m.getPriceD()).multiply(bd100).intValue()).sum());
		}
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		if (this.hasAmountStored(orderin)) {
			// 如果订单总金额大于账户剩余金额，单位分
			if (orderm.getTotal() > account.getTotalMoney()) {
				log.error("热线工单-购物车-提交订单>>订单[{}]总金额[{}]大于账户剩余金额[{}]", orderm.getOrderNo(), orderm.getTotal(),
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
			orderm.setPayStatus(CommonConstant.PAY_STATUS_1);// 已收款
		} else {
			orderm.setPayStatus(CommonConstant.PAY_STATUS_0);// 未收款
		}
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
			log.error("热线工单-购物车-提交订单>>调用扣减库存服务接口失败>>{}", productReduce);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "提交订单失败，请稍后重试。");
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
				log.error("热线工单-购物车-提交订单>>调用顾客[{}]账户消费服务接口失败>>{}", orderm.getMemberCardNo(), customerAmountOperation);
				// 恢复库存
				ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
				// 如果调用服务接口失败
				if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
					log.error("热线工单-购物车-提交订单>>恢复库存失败>>{}", increaseStock);
					this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL,
							ProductClient.INCREASE_STOCK_URL, orderm.getStaffCode(), orderm.getOrderNo());
				}
				return ComResponse.fail(ResponseCodeEnums.ERROR, "提交订单失败，请稍后重试。");
			}
		}
		log.info("订单: {}", JSON.toJSONString(orderm, true));
		log.info("订单明细: {}", JSON.toJSONString(orderdetailList, true));
		// 调用创建订单服务接口
		ComResponse<?> submitOrder = this.orderFeignClient.submitOrder(new OrderRequest(orderm, orderdetailList));
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(submitOrder.getCode())) {
			log.error("热线工单-购物车-提交订单>>创建订单失败[订单号：{}]>>{}", orderm.getOrderNo(), submitOrder);
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("热线工单-购物车-提交订单>>恢复库存失败>>{}", increaseStock);
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
					log.error("热线工单-购物车-提交订单>>恢复账户失败>>{}", customerAmountOperation2);
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
			log.error("热线工单-购物车-提交订单>>更新顾客信息失败>>{}", createUpdateMember);
			this.orderCommonService.insert(createUpdateMember, MemberFien.SUFFIX_URL,
					MemberFien.DEAL_ORDER_CREATE_UPDATE_MEMBER_DATA_URL, orderm.getStaffCode(), orderm.getOrderNo());
		}
		// 再次调用顾客账户余额
		maresponse = this.memberFien.getMemberAmount(orderm.getMemberCardNo());
		return ComResponse.success(new OrderOut(orderm.getReveiverAddress(), orderm.getReveiverName(),
				orderm.getReveiverTelphoneNo(), BigDecimal.valueOf(orderm.getTotal()).divide(bd100).doubleValue(),
				BigDecimal.valueOf(maresponse.getData().getTotalMoney()).divide(bd100).doubleValue(),
				orderm.getOrderNo(), orderm.getCreateTime()));
	}

	/**
	 * 初始化订单
	 * 
	 * @param orderm 订单
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
		orderm.setAmountStored(0);
		orderm.setAmountRedEnvelope(0);
		orderm.setAmountCoupon(0);
		orderm.setPointsDeduction(0);
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
	}

	/**
	 * @param orderin 订单
	 * @return 是否使用账户金额
	 * @author zhangweiwei
	 * @date 2021年2月2日,下午7:15:59
	 */
	private boolean hasAmountStored(OrderIn orderin) {
		return orderin.getAmountStored() != null && orderin.getAmountStored() > 0D
				&& Integer.compare(PayType.PAY_TYPE_1.getCode(), orderin.getPayType()) == 0;
	}

	@PostMapping("/v1/updateorder")
	@ApiOperation(value = "订单列表-编辑", notes = "订单列表-编辑")
	public ComResponse<String> updateOrder(@RequestBody UpdateOrderIn orderin) {
		// 判断是否传入订单号
		if (!StringUtils.hasText(orderin.getOrderNo())) {
			log.error("订单列表-编辑>>订单号不能为空>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单号不能为空。");
		}
		// 判断是否传入订单明细
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			log.error("订单列表-编辑>>订单明细集合里没有任何元素>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按订单编号查询订单信息
		ComResponse<OrderM> oresponse = this.orderFeignClient.queryOrder(orderin.getOrderNo());
		// 如果调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(oresponse.getCode())) {
			log.error("订单列表-编辑>>调用查询订单[{}]信息接口失败>>{}", orderin.getOrderNo(), oresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		OrderM orderm = oresponse.getData();
		// 如果不存在
		if (orderm == null) {
			log.error("订单列表-编辑>>调用查询订单[{}]信息接口失败>>{}", orderin.getOrderNo(), oresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		// 按顾客号查询顾客账号
		ComResponse<MemberAmountDto> maresponse = this.memberFien.getMemberAmount(orderm.getMemberCardNo());
		// 如果调用服务异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(maresponse.getCode())) {
			log.error("订单列表-编辑>>找不到该顾客[{}]账号>>{}", orderm.getMemberCardNo(), maresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到该顾客账号[%s]", maresponse.getMessage()));
		}
		MemberAmountDto account = maresponse.getData();
		if (account == null) {
			log.error("订单列表-编辑>>找不到该顾客[{}]账号>>{}", orderm.getMemberCardNo(), account);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
		}
		orderm.setUpdateTime(new Date());// 修改时间
		orderm.setUpdateCode(QueryIds.userNo.get());// 修改人编码
		orderm.setCreateTime(orderm.getUpdateTime());
		orderm.setStaffCode(orderm.getUpdateCode());
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(orderm.getUpdateCode());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("订单列表-编辑>>找不到该坐席[{}]信息>>{}", orderm.getUpdateCode(), sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到该坐席信息[%s]", sresponse.getMessage()));
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("订单列表-编辑>>找不到该坐席[{}]信息>>{}", orderm.getUpdateCode(), sresponse);
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
			log.error("订单列表-编辑>>找不到该坐席[{}]所在部门[{}]的财务归属>>{}", orderm.getStaffCode(), staffInfo.getDepartId(),
					dresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到该坐席的财务归属[%s]", dresponse.getMessage()));
		}
		DepartDto depart = dresponse.getData();
		if (depart == null) {
			log.error("订单列表-编辑>>找不到该坐席[{}]所在部门[{}]的财务归属>>{}", orderm.getStaffCode(), staffInfo.getDepartId(),
					dresponse);
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
		BigDecimal bd100 = BigDecimal.valueOf(100);// 元转分
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
				log.error("订单列表-编辑>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到商品信息[%s]", presponse.getMessage()));
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("订单列表-编辑>>找不到商品[{}]信息>>{}", productCodes, presponse);
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
					log.error("订单列表-编辑>>该商品[{}]已下架>>{}", p);
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
				od.setPackageunit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				tuples.add(new Tuple(od.getProductCode(), od.getProductCount()));// 商品总数
				od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
				od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				orderdetailList.add(od);
			}
			orderm.setTotal(orderm.getTotal() + orderdetailList.stream().mapToInt(OrderDetail::getTotal).sum());
			orderm.setCash(orderm.getCash() + orderdetailList.stream().mapToInt(OrderDetail::getCash).sum());
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
				log.error("订单列表-编辑>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到套餐信息[%s]", mresponse.getMessage()));
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("订单列表-编辑>>找不到套餐[{}]信息>>{}", mealNos, mresponse);
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
					od.setPackageunit(in.getPackagingUnit());// 包装单位
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
			orderm.setTotal(orderm.getTotal()
					+ mlist.stream().mapToInt(m -> BigDecimal.valueOf(m.getPriceD()).multiply(bd100).intValue()).sum());
			orderm.setCash(orderm.getCash()
					+ mlist.stream().mapToInt(m -> BigDecimal.valueOf(m.getPriceD()).multiply(bd100).intValue()).sum());
		}
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		// 如果订单总金额大于账户剩余金额，单位分
		if (orderm.getTotal() > account.getTotalMoney()) {
			log.error("订单列表-编辑>>订单[{}]总金额[{}]大于账户剩余金额[{}]", orderm.getOrderNo(), orderm.getTotal(),
					account.getTotalMoney());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "账户余额不足。");
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
			log.error("订单列表-编辑>>调用扣减库存服务接口失败>>{}", productReduce);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("修改订单失败[%s]", productReduce.getMessage()));
		}
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
			log.error("订单列表-编辑>>调用顾客[{}]账户消费服务接口失败>>{}", orderm.getMemberCardNo(), customerAmountOperation);
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("订单列表-编辑>>恢复库存失败>>{}", increaseStock);
				this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL, ProductClient.INCREASE_STOCK_URL,
						orderm.getStaffCode(), orderm.getOrderNo());
			}
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("修改订单失败[%s]", customerAmountOperation.getMessage()));
		}
		log.info("订单: {}", JSON.toJSONString(orderm, true));
		log.info("订单明细: {}", JSON.toJSONString(orderdetailList, true));
		// 调用修改订单服务接口
		ComResponse<?> updateOrder = this.orderFeignClient.updateOrder(new OrderRequest(orderm, orderdetailList));
		// 如果调用服务接口失败
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(updateOrder.getCode())) {
			log.error("订单列表-编辑>>修改订单失败[订单号：{}]>>{}", orderm.getOrderNo(), updateOrder);
			// 恢复库存
			ComResponse<?> increaseStock = this.productClient.increaseStock(orderProduct);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(increaseStock.getCode())) {
				log.error("订单列表-编辑>>恢复库存失败>>{}", increaseStock);
				this.orderCommonService.insert(orderProduct, ProductClient.SUFFIX_URL, ProductClient.INCREASE_STOCK_URL,
						orderm.getStaffCode(), orderm.getOrderNo());
			}
			// 恢复账户
			memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 退回
			memberAmountDetail.setRemark("订单列表-编辑:退回金额");// 备注
			ComResponse<?> customerAmountOperation2 = this.memberFien.customerAmountOperation(memberAmountDetail);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation2.getCode())) {
				log.error("订单列表-编辑>>恢复账户失败>>{}", customerAmountOperation2);
				this.orderCommonService.insert(memberAmountDetail, MemberFien.SUFFIX_URL,
						MemberFien.CUSTOMER_AMOUNT_OPERATION_URL, orderm.getStaffCode(), orderm.getOrderNo());
			}
			return ComResponse.fail(ResponseCodeEnums.ERROR, "修改订单失败，请稍后重试。");
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
			log.error("热线工单-购物车-提交订单>>更新顾客信息失败>>{}", createUpdateMember);
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
			log.error("订单列表-异常处理-补发订单>>订单号不能为空>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单号不能为空。");
		}
		// 判断是否传入订单明细
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			log.error("订单列表-异常处理-补发订单>>订单明细集合里没有任何元素>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按订单编号查询订单信息
		ComResponse<OrderM> oresponse = this.orderFeignClient.queryOrder(orderin.getOrderNo());
		// 如果调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(oresponse.getCode())) {
			log.error("订单列表-异常处理-补发订单>>调用查询订单[{}]信息接口失败>>{}", orderin.getOrderNo(), oresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "该订单信息不存在。");
		}
		OrderM orderm = oresponse.getData();
		// 如果不存在
		if (orderm == null) {
			log.error("订单列表-异常处理-补发订单>>调用查询订单[{}]信息接口失败>>{}", orderin.getOrderNo(), oresponse);
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
			log.error("订单列表-异常处理-补发订单>>找不到该坐席[{}]信息>>{}", orderm.getUpdateCode(), sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("找不到该坐席信息[%s]", sresponse.getMessage()));
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		if (staffInfo == null) {
			log.error("订单列表-异常处理-补发订单>>找不到该坐席[{}]信息>>{}", orderm.getUpdateCode(), sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		return ComResponse.success();
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
	private RedisUtil redisUtil;
	@Resource
	private IOrderCommonService orderCommonService;
}

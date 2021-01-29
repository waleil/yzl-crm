package cn.net.yzl.crm.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.member.MemberAddressClient;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.constant.ObtainType;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.RedisKeys;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import cn.net.yzl.order.model.vo.order.OrderIn;
import cn.net.yzl.order.model.vo.order.OrderRequest;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单restful控制器
 * 
 * @author zhangweiwei
 * @date 2021年1月16日,下午12:12:17
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderRestController {
	@Resource
	private OrderFeignClient orderFeignClient;
	@Resource
	private MealClient mealClient;
	@Resource
	private ProductClient productClient;
	@Resource
	private MemberFien memberFien;
	@Resource
	private MemberAddressClient memberAddressClient;
	@Resource
	private EhrStaffClient ehrStaffClient;
	@Resource
	private RedisUtil redisUtil;

	@PostMapping("/v1/submitorder")
	@ApiOperation(value = "热线工单-购物车-提交订单")
	public ComResponse<OrderOut> submitOrder(@RequestBody OrderIn orderin) {
		OrderM orderm = new OrderM();// 订单信息
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
		orderm.setInvoiceFlag(CommonConstant.INVOICE_F);// 不开票
		orderm.setRelationReissueOrderNo("0");
		orderm.setStaffCode(QueryIds.userNo.get());// 下单坐席编码
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
		ComResponse<List<ReveiverAddressDto>> raresponse = this.memberAddressClient
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
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, staffInfo.getWorkCode(),
				orderm.getStaffCode(), RedisKeys.CREATE_ORDER_NO, 4));// 使用redis生成订单号
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(staffInfo.getDepartId());// 下单坐席所属部门id
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
				log.error("热线工单-购物车-提交订单>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("热线工单-购物车-提交订单>>找不到商品[{}]信息>>{}", productCodes, presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			if (plist.size() != productCodeList.size()) {
				log.error("热线工单-购物车-提交订单>>订单的商品编码总数[{}]与商品查询接口的商品编码总数[{}]不一致", productCodeList.size(), plist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的商品部分已下架。");
			}
			// 将商品编码作为key，商品对象作为value
			Map<String, ProductMainDTO> pmap = plist.stream()
					.collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
			// 组装订单明细信息
			List<OrderDetail> result = orderProductList.stream().map(in -> {
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
				ProductMainDTO p = pmap.get(in.getProductCode());
				od.setProductCode(p.getProductCode());// 商品唯一标识
				od.setProductNo(p.getProductNo());// 商品编码
				od.setProductName(p.getName());// 商品名称
				od.setProductBarCode(p.getBarCode());// 产品条形码
				od.setProductUnitPrice(BigDecimal.valueOf(Double.valueOf(p.getSalePrice()))
						.multiply(BigDecimal.valueOf(100)).intValue());// 商品单价
				od.setProductCount(in.getProductCount());// 商品数量
				od.setUnit(p.getUnit());// 单位
				od.setSpec(String.valueOf(p.getTotalUseNum()));// 商品规格
				od.setPackageunit(p.getPackagingUnit());// 包装单位
				productStockMap.put(od.getProductCode(), p.getStock());// 库存
				// 如果是非赠品
				if (CommonConstant.GIFT_FLAG_0.equals(od.getGiftFlag())) {
					od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
					od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
				} else {// 如果是赠品，将金额设置为0
					od.setTotal(0);// 实收金额，单位分
					od.setCash(0);// 应收金额，单位分
				}
				return od;
			}).collect(Collectors.toList());
			orderdetailList.addAll(result);
			orderm.setTotal(orderm.getTotal() + result.stream().mapToInt(OrderDetail::getTotal).sum());
			orderm.setCash(orderm.getCash() + result.stream().mapToInt(OrderDetail::getCash).sum());
		}
		// 获取套餐
		List<OrderDetailIn> ordermealList = orderdetailMap.get(CommonConstant.MEAL_FLAG_1);
		// 如果有套餐信息
		if (!CollectionUtils.isEmpty(ordermealList)) {
			// 收集套餐编码
			List<String> mealNoList = ordermealList.stream().map(OrderDetailIn::getMealNo).distinct()
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
			if (mlist.size() != mealNoList.size()) {
				log.error("热线工单-购物车-提交订单>>订单的套餐编码总数[{}]与套餐查询接口的套餐编码总数[{}]不一致", mealNoList.size(), mlist.size());
				return ComResponse.fail(ResponseCodeEnums.ERROR, "查询的套餐部分已下架。");
			}
			for (ProductMealListDTO meal : mlist) {
				// 如果套餐里没有商品
				if (CollectionUtils.isEmpty(meal.getMealProductList())) {
					log.error("热线工单-购物车-提交订单>>该套餐没有包含商品信息>>{}", meal);
					return ComResponse.fail(ResponseCodeEnums.ERROR, "该套餐没有包含商品信息。");
				}
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
					od.setMealCount(in.getProductNum());// 套餐数量
					od.setMealPrice(meal.getPrice());// 套餐价格
					od.setProductCode(in.getProductCode());// 商品唯一标识
					od.setProductNo(in.getProductNo());// 商品编码
					od.setProductName(in.getName());// 商品名称
					od.setProductBarCode(in.getBarCode());// 产品条形码
					od.setProductUnitPrice(in.getSalePrice());// 商品单价，单位分=元*100
					od.setProductCount(in.getProductNum());// 商品数量
					od.setUnit(in.getUnit());// 单位
					od.setSpec(String.valueOf(in.getTotalUseNum()));// 商品规格
					productStockMap.put(od.getProductCode(), in.getStock());// 库存
					// 如果是非赠品
					if (CommonConstant.GIFT_FLAG_0.equals(od.getGiftFlag())) {
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
				// 套餐价
				BigDecimal mealPrice = BigDecimal.valueOf(meal.getPrice());
				orderdetailList.addAll(result.stream().map(od -> {
					BigDecimal price = mealPrice.multiply(BigDecimal.valueOf(od.getProductUnitPrice()))
							.divide(orderdetailTotal, 2, BigDecimal.ROUND_HALF_UP);
					// 如果是非赠品
					if (CommonConstant.GIFT_FLAG_0.equals(od.getGiftFlag())) {
						od.setProductUnitPrice(price.intValue());
					} else {// 如果是赠品，将金额设置为0
						od.setProductUnitPrice(0);
					}
					return od;
				}).collect(Collectors.toList()));
			}
			orderm.setTotal(orderm.getTotal() + mlist.stream().mapToInt(ProductMealListDTO::getPrice).sum());
			orderm.setCash(orderm.getCash() + mlist.stream().mapToInt(ProductMealListDTO::getPrice).sum());
		}
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		// 如果订单总金额大于账户剩余金额，单位分
		if (orderm.getTotal() > account.getTotalMoney()) {
			log.error("热线工单-购物车-提交订单>>订单[{}]总金额[{}]大于账户剩余金额[{}]", orderm.getOrderNo(), orderm.getTotal(),
					account.getTotalMoney());
			return ComResponse.fail(ResponseCodeEnums.ERROR, "账户余额不足。");
		}
		// 将订单明细按商品编码进行分组，key为商品编码，value为订单明细集合
		Map<String, List<OrderDetail>> productMap = orderdetailList.stream()
				.collect(Collectors.groupingBy(OrderDetail::getProductCode));
		// 统计订单明细中的每类商品的总数，key为商品编码，value为购买商品总数
		Map<String, Integer> productCountMap = productMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
				v -> v.getValue().stream().mapToInt(OrderDetail::getProductCount).sum()));
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
		// 如果是款到发货
		if (String.valueOf(orderin.getPayType()).equals(String.valueOf(CommonConstant.PAY_TYPE_1))) {
			orderm.setOrderNature(CommonConstant.ORDER_NATURE_F);// 非免审
			orderm.setPayStatus(CommonConstant.PAY_STATUS_1);// 已收款
		}
		orderm.setRemark(orderin.getRemark());// 订单备注
		orderm.setReveiverAddressNo(orderin.getReveiverAddressNo());// 配送地址唯一标识
		orderm.setReveiverAddress(String.format("%s %s %s %s", reveiverAddress.getMemberProvinceName(),
				reveiverAddress.getMemberCityName(), reveiverAddress.getMemberCountyName(),
				reveiverAddress.getMemberAddress()));// 收货人地址
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
			return ComResponse.fail(ResponseCodeEnums.ERROR, "提交订单失败，请稍后重试。");
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
				// TODO zww 插入本地消息记录表
			}
			// 恢复账户
			memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 退回
			memberAmountDetail.setRemark("热线工单-购物车-提交订单:退回金额");// 备注
			ComResponse<?> customerAmountOperation2 = this.memberFien.customerAmountOperation(memberAmountDetail);
			// 如果调用服务接口失败
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation2.getCode())) {
				log.error("热线工单-购物车-提交订单>>恢复账户失败>>{}", customerAmountOperation2);
				// TODO zww 插入本地消息记录表
			}
			return ComResponse.fail(ResponseCodeEnums.ERROR, "提交订单失败，请稍后重试。");
		}
		log.info("热线工单-购物车-提交订单>>创建订单成功[订单号：{}]", orderm.getOrderNo());
		// 再次调用顾客账户余额
		maresponse = this.memberFien.getMemberAmount(orderm.getMemberCardNo());
		return ComResponse.success(new OrderOut(orderm.getReveiverAddress(), orderm.getReveiverName(),
				orderm.getReveiverTelphoneNo(),
				BigDecimal.valueOf(orderm.getTotal()).divide(BigDecimal.valueOf(100)).doubleValue(),
				BigDecimal.valueOf(maresponse.getData().getTotalMoney()).divide(BigDecimal.valueOf(100)).doubleValue(),
				orderm.getOrderNo()));
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ApiModel(description = "订单")
	public static class OrderOut {
		@ApiModelProperty(value = "收货人地址")
		private String reveiverAddress;
		@ApiModelProperty(value = "收货人姓名")
		private String reveiverName;
		@ApiModelProperty(value = "收货人电话")
		private String reveiverTelphoneNo;
		@ApiModelProperty(value = "实收金额")
		private double total;
		@ApiModelProperty(value = "账户余额")
		private double totalMoney;
		@ApiModelProperty(value = "订单号")
		private String orderNo;
	}
}

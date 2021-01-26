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

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberAmount;
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
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
	private EhrStaffClient ehrStaffClient;
	@Resource
	private RedisUtil redisUtil;

	@PostMapping("/v1/submitorder")
	@ApiOperation(value = "热线工单-购物车-提交订单")
	public ComResponse<Object> submitOrder(@RequestBody OrderIn orderin) {
		// TODO zww 热线工单-购物车-提交订单
		OrderM orderm = new OrderM();// 订单信息
		orderm.setTotal(0);// 实收金额=应收金额+预存
		orderm.setCash(0);// 应收金额=订单总额+邮费-优惠
		orderm.setTotalAll(0);// 订单总额
		orderm.setCash1(0);// 预存金额
		orderm.setSpend(0);// 消费金额=订单总额-优惠
		// 如果订单里没有商品
		if (CollectionUtils.isEmpty(orderin.getOrderDetailIns())) {
			log.error("热线工单-购物车-提交订单>>订单明细集合里没有任何元素>>{}", orderin);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "订单里没有商品或套餐信息。");
		}
		// 按顾客号查询顾客信息
		GeneralResult<Member> mresult = this.memberFien.getMember(orderin.getMemberCardNo());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(mresult.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客信息>>{}", mresult);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客信息。");
		}
		Member member = mresult.getData();
		if (member == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客信息>>{}", member);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客信息。");
		}
		MemberAmount account = member.getMember_amount();// 顾客账户
		if (account == null) {
			log.error("热线工单-购物车-提交订单>>找不到该顾客账号>>{}", account);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该顾客账号。");
		}
		// 按员工号查询员工信息
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席信息>>{}", sresponse);
			return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
		}
		StaffImageBaseInfoDto staffInfo = sresponse.getData();
		orderm.setStaffCode(QueryIds.userNo.get());// 下单坐席编码
		orderm.setOrderNo(this.redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, staffInfo.getWorkCode(),
				orderm.getStaffCode(), RedisKeys.CREATE_ORDER_NO, 4));// 使用redis生成订单号
		// 按部门id查询部门信息
		ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(staffInfo.getDepartId());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
			log.error("热线工单-购物车-提交订单>>找不到该坐席的财务归属>>{}", dresponse);
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
				log.error("热线工单-购物车-提交订单>>找不到商品信息>>{}", presponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到商品信息。");
			}
			List<ProductMainDTO> plist = presponse.getData();
			if (CollectionUtils.isEmpty(plist)) {
				log.error("热线工单-购物车-提交订单>>找不到商品信息>>{}", presponse);
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
				if (CommonConstant.GIFT_FLAG_0 == od.getGiftFlag()) {
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
				log.error("热线工单-购物车-提交订单>>找不到套餐信息>>{}", mresponse);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到套餐信息。");
			}
			List<ProductMealListDTO> mlist = mresponse.getData();
			if (CollectionUtils.isEmpty(mlist)) {
				log.error("热线工单-购物车-提交订单>>找不到套餐信息>>{}", mresponse);
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
					od.setProductName(in.getName());// 商品名称
					od.setProductBarCode(in.getBarCode());// 产品条形码
					od.setProductUnitPrice(
							BigDecimal.valueOf(in.getSalePrice()).multiply(BigDecimal.valueOf(100)).intValue());// 商品单价
					od.setProductCount(in.getProductNum());// 商品数量
					od.setUnit(in.getUnit());// 单位
					od.setSpec(String.valueOf(in.getTotalUseNum()));// 商品规格
					productStockMap.put(od.getProductCode(), in.getStock());// 库存
					// 如果是非赠品
					if (CommonConstant.GIFT_FLAG_0 == od.getGiftFlag()) {
						od.setTotal(od.getProductUnitPrice() * od.getProductCount());// 实收金额，单位分
						od.setCash(od.getProductUnitPrice() * od.getProductCount());// 应收金额，单位分
					} else {// 如果是赠品，将金额设置为0
						od.setTotal(0);// 实收金额，单位分
						od.setCash(0);// 应收金额，单位分
					}
					return od;
				}).collect(Collectors.toList());

				orderdetailList.addAll(result);
			}
			orderm.setTotal(orderm.getTotal() + mlist.stream().mapToInt(ProductMealListDTO::getPrice).sum());
			orderm.setCash(orderm.getCash() + mlist.stream().mapToInt(ProductMealListDTO::getPrice).sum());
		}
		orderm.setTotalAll(orderm.getTotal());
		orderm.setSpend(orderm.getCash());
		// 如果订单总金额大于账户剩余金额
		if (orderm.getTotal() > account.getTotalMoney()) {
			log.error("热线工单-购物车-提交订单>>订单总金额[{}]大于账户剩余金额[{}]", orderm.getTotal(), account.getTotalMoney());
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
				log.error("热线工单-购物车-提交订单>>该商品购买总数[{}]大于商品库存[{}]", entry.getValue(), pstock);
				return ComResponse.fail(ResponseCodeEnums.ERROR, "该商品库存不足。");
			}
		}
		orderm.setWorkOrderNo(orderin.getWorkOrderNo());// 工单号
		orderm.setWorkBatchNo(orderin.getWorkBatchNo());// 工单流水号
		orderm.setPayType(orderin.getPayType());// 支付方式
		// 如果是款到发货
		if (String.valueOf(orderin.getPayType()).equals(String.valueOf(CommonConstant.PAY_TYPE_1))) {
			orderm.setOrderNature(CommonConstant.ORDER_NATURE_F);// 非面审
			orderm.setPayStatus(CommonConstant.PAY_STATUS_1);// 已收款
		}
		orderm.setRemark(orderin.getRemark());// 订单备注
		orderm.setReveiverAddressNo(orderin.getReveiverAddressNo());// 配送地址唯一标识
		orderm.setReveiverAddress(orderin.getReveiverAddress());// 收货人地址
		orderm.setReveiverName(orderin.getReveiverName());// 收货人姓名
		orderm.setReveiverTelphoneNo(orderin.getReveiverTelphoneNo());// 收货人电话
		orderm.setReveiverProvince(orderin.getReveiverProvince());// 省份编码
		orderm.setReveiverProvinceName(orderin.getReveiverProvinceName());// 省份名称
		orderm.setReveiverCity(orderin.getReveiverCity());// 城市编码
		orderm.setReveiverCityName(orderin.getReveiverCityName());// 城市名称
		orderm.setReveiverArea(orderin.getReveiverArea());// 区县编码
		orderm.setReveiverAreaName(orderin.getReveiverAreaName());// 区县名称
		orderm.setMediaChannel(orderin.getMediaChannel());// 媒介渠道
		orderm.setMediaName(orderin.getMediaName());// 媒介名称
		orderm.setMediaNo(orderin.getMediaNo());// 媒介唯一标识
		orderm.setMediaType(orderin.getMediaType());// 媒介类型
		orderm.setMemberCardNo(orderin.getMemberCardNo());// 顾客卡号
		orderm.setMemberName(member.getMember_name());// 顾客姓名
		orderm.setStaffName(staffInfo.getName());// 下单坐席姓名
		orderm.setDepartId(staffInfo.getDepartId());// 下单坐席所属部门id
		orderm.setUpdateCode(orderm.getStaffCode());// 更新人编号
		orderm.setUpdateName(orderm.getStaffName());// 更新人姓名
		// 找顾客确认
		orderm.setMemberLevelBefor(null);// 单前顾客级别
		orderm.setMemberTypeBefor(null);// 单前顾客类型
		// 调用锁定库存接口
		// 创建订单
		//
		return ComResponse.success(orderm);
	}

}

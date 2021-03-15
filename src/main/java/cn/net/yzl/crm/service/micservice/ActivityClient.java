package cn.net.yzl.crm.service.micservice;

import java.util.List;

import cn.net.yzl.activity.model.dto.MemberRedBagDto;
import cn.net.yzl.crm.dto.order.MemberCouponInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.activity.model.requestModel.AccountHistoryRequest;
import cn.net.yzl.activity.model.requestModel.AccountRequest;
import cn.net.yzl.activity.model.requestModel.AccountWithOutPageRequest;
import cn.net.yzl.activity.model.requestModel.CalculateOrderRequest;
import cn.net.yzl.activity.model.requestModel.CalculateRequest;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.ListAccountRequest;
import cn.net.yzl.activity.model.requestModel.OrderSubmitRequest;
import cn.net.yzl.activity.model.requestModel.ProductDiscountRequest;
import cn.net.yzl.activity.model.requestModel.ProductListDiscountRequest;
import cn.net.yzl.activity.model.requestModel.RejectionOrderRequest;
import cn.net.yzl.activity.model.responseModel.CalculationOrderResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountHistoryResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountResponse;
import cn.net.yzl.activity.model.responseModel.MemberCouponResponse;
import cn.net.yzl.activity.model.responseModel.MemberIntegralRecordsResponse;
import cn.net.yzl.activity.model.responseModel.MemberRedBagRecordsResponse;
import cn.net.yzl.activity.model.responseModel.OrderSubmitResponse;
import cn.net.yzl.activity.model.responseModel.ProductDiscountResponse;
import cn.net.yzl.activity.model.responseModel.ProductListDiscountResponse;
import cn.net.yzl.activity.model.responseModel.ProductPriceResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.dmc.ActivityDetailResponse;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import cn.net.yzl.crm.dto.dmc.MemberLevelResponse;
import cn.net.yzl.crm.dto.dmc.PageModel;
import cn.net.yzl.crm.dto.dmc.TaskDto;
import io.swagger.annotations.ApiOperation;

@FeignClient(name = "activityDB", url = "${api.gateway.url}/activityDB")
public interface ActivityClient {

	Logger logger = LoggerFactory.getLogger(ActivityClient.class);

	@ApiOperation(value = "顾客拒收订单，将发放的、未使用的优惠券、积分扣除")
	@PostMapping("db/v1/rejectionOrder")
	ComResponse<Boolean> rejectionOrder(@RequestBody RejectionOrderRequest request);

	@ApiOperation(value = "顾客积分明细表")
	@PostMapping("db/v1/getMemberIntegralRecords")
	ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecords(@RequestBody AccountRequest request);

	/**
	 * 根据多个顾客卡号 查询查询积分明细表
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return 积分历史记录
	 * @author zhangweiwei
	 * @date 2021年3月9日,下午2:14:50
	 */
	@PostMapping("/db/v1/getMemberIntegralRecordsByMemberCards")
	ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecordsByMemberCards(
			@RequestBody ListAccountRequest request);

	@ApiOperation(value = "顾客积分明细表 - 不分页")
	@PostMapping("db/v1/getMemberIntegralRecordsWithOutPage")
	ComResponse<List<MemberIntegralRecordsResponse>> getMemberIntegralRecordsWithOutPage(
			@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "顾客红包明细表")
	@PostMapping("db/v1/getMemberRedBagRecords")
	ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecords(@RequestBody AccountRequest request);

	/**
	 * 根据多个顾客卡号 查询顾客红包明细表
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return 红包历史记录
	 * @author zhangweiwei
	 * @date 2021年3月9日,下午2:21:02
	 */
	@PostMapping("/db/v1/getMemberRedBagRecordsByMemberCards")
	ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecordsByMemberCards(
			@RequestBody ListAccountRequest request);

	@ApiOperation(value = "顾客红包明细表 - 不分页")
	@PostMapping("db/v1/getMemberRedBagRecordsWithOutPage")
	ComResponse<List<MemberRedBagRecordsResponse>> getMemberRedBagRecordsWithOutPage(
			@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "顾客优惠券明细表")
	@PostMapping("db/v1/getMemberCoupon")
	ComResponse<Page<MemberCouponResponse>> getMemberCoupon(@RequestBody AccountRequest request);

	/**
	 * 根据多个顾客卡号 查询顾客优惠券明细表
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return 优惠券历史记录
	 * @author zhangweiwei
	 * @date 2021年3月9日,下午2:11:48
	 */
	@PostMapping("/db/v1/getMemberCouponByMemberCards")
	ComResponse<Page<MemberCouponResponse>> getMemberCouponByMemberCards(@RequestBody ListAccountRequest request);

	@ApiOperation(value = "顾客优惠券明细表 - 不分页")
	@PostMapping("db/v1/getMemberCouponWithOutPage")
	ComResponse<List<MemberCouponResponse>> getMemberCouponWithOutPage(@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "根据单个会员卡号获取 每个顾客的优惠券 积分 红包")
	@GetMapping("db/v1/getAccountByMemberCard")
	ComResponse<MemberAccountResponse> getAccountByMemberCard(@RequestParam("memberCard") String memberCard);

	@ApiOperation(value = "根据单个会员卡号获取 每个顾客的优惠券 积分 红包的历史记录")
	@PostMapping("db/v1/getAccountHistoryByMemberCard")
	ComResponse<Page<MemberAccountHistoryResponse>> getAccountHistoryByMemberCard(
			@RequestBody AccountHistoryRequest request);

	@ApiOperation(value = "根据多个商品唯一编码 查询当前的优惠方式、可用优惠券")
	@PostMapping("db/v1/getProductDiscountByProductCodes")
	ComResponse<ProductListDiscountResponse> getProductListDiscount(@RequestBody ProductListDiscountRequest request);

	@ApiOperation(value = "根据商品唯一编码 查询当前的优惠方式、可用优惠券")
	@PostMapping("db/v1/getProductDiscountByProductCode")
	ComResponse<ProductDiscountResponse> getProductDiscount(@RequestBody ProductDiscountRequest request);

	@ApiOperation(value = "计算金额")
	@PostMapping("db/v1/calculate")
	ComResponse<ProductPriceResponse> calculate(@RequestBody CalculateRequest request);

	/**
	 * 购物车计算金额
	 * 
	 * @param request {@link CalculateOrderRequest}
	 * @return {@link CalculationOrderResponse}
	 * @author zhangweiwei
	 * @date 2021年3月15日,下午4:48:46
	 */
	@PostMapping("db/v1/calculateOrder")
	ComResponse<CalculationOrderResponse> calculateOrder(@RequestBody CalculateOrderRequest request);

	@ApiOperation(value = "校验订单金额")
	@PostMapping("db/v1/checkOrderAmount")
	ComResponse<List<ProductPriceResponse>> checkOrderAmount(@RequestBody CheckOrderAmountRequest request);

	@GetMapping("db/v1/launchManage/getAllLaunchManage")
	ComResponse<List<LaunchManageDto>> getAllLaunchManage();

	@GetMapping("db/v1/launchManage/getLaunchManageByRelationBusNo")
	ComResponse<List<LaunchManageDto>> getLaunchManageByRelationBusNo(
			@RequestParam("relationBusNo") Long relationBusNo);

	@ApiOperation(value = "商品促销活动-获取所有的活动")
	@GetMapping("db/v1/productSales/getActivityList")
	ComResponse<List<ActivityDetailResponse>> getActivityList();

	@ApiOperation(value = "会员管理-会员级别管理-会员级别设置列表")
	@PostMapping("db/v1/memberLevelManager/getMemberLevelPages")
	ComResponse<Page<MemberLevelResponse>> getMemberLevelPages(@RequestBody PageModel request);

	@ApiOperation(value = "根据员工群Id 查询任务")
	@GetMapping("db/v1/getMarketTarget")
	ComResponse<TaskDto> getMarketTarget(@RequestParam("groupId") Long staffGroupBusNo);

	default TaskDto getMarketTargetDefault(Long groupId) {
		try {
			ComResponse<TaskDto> comResponse = getMarketTarget(groupId);
			if (null == comResponse || 200 != comResponse.getCode()) {
				return null;
			}
			return comResponse.getData();
		} catch (Exception e) {
			logger.error("根据员工群Id 查询任务：", e);
		}
		return null;
	}

	/**
	 * 投放管理- 根据业务主键查询广告
	 * 
	 * @param busNo 业务主键
	 * @return 广告
	 * @author zhangweiwei
	 * @date 2021年2月20日,下午11:37:35
	 */
	@GetMapping("/db/v1/launchManage/getLaunchManageByBusNo")
	ComResponse<LaunchManageDto> getLaunchManageByBusNo(@RequestParam Integer busNo);

	/**
	 * 提交订单送积分和优惠券
	 * 
	 * @param request {@link OrderSubmitRequest}
	 * @return {@link OrderSubmitResponse}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午5:08:19
	 */
	@PostMapping("/db/v1/orderSubmit")
	ComResponse<OrderSubmitResponse> orderSubmit(@RequestBody OrderSubmitRequest request);

	/**
	 * 提交订单送积分和优惠券
	 *
	 * @param memberCards
	 * @return {@link OrderSubmitResponse}
	 * @author zhangweiwei
	 * @date 2021年2月21日,上午5:08:19
	 */
	@PostMapping("/db/v1/getAccountByMemberCards")
	ComResponse<List<MemberCouponInfoDTO>> getAccountByMemberCards(@RequestBody List<String> memberCards);

	@ApiOperation(value = "根据顾客卡号获取顾客红包信息")
	@GetMapping("db/v1/memberRedBag/getDtoByMemberCard")
	ComResponse<MemberRedBagDto> getDtoByMemberCard(@RequestParam("memberCard") String memberCard);
}

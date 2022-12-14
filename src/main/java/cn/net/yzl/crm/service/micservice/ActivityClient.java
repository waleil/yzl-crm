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

	@ApiOperation(value = "????????????????????????????????????????????????????????????????????????")
	@PostMapping("db/v1/rejectionOrder")
	ComResponse<Boolean> rejectionOrder(@RequestBody RejectionOrderRequest request);

	@ApiOperation(value = "?????????????????????")
	@PostMapping("db/v1/getMemberIntegralRecords")
	ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecords(@RequestBody AccountRequest request);

	/**
	 * ???????????????????????? ???????????????????????????
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return ??????????????????
	 * @author zhangweiwei
	 * @date 2021???3???9???,??????2:14:50
	 */
	@PostMapping("/db/v1/getMemberIntegralRecordsByMemberCards")
	ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecordsByMemberCards(
			@RequestBody ListAccountRequest request);

	@ApiOperation(value = "????????????????????? - ?????????")
	@PostMapping("db/v1/getMemberIntegralRecordsWithOutPage")
	ComResponse<List<MemberIntegralRecordsResponse>> getMemberIntegralRecordsWithOutPage(
			@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "?????????????????????")
	@PostMapping("db/v1/getMemberRedBagRecords")
	ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecords(@RequestBody AccountRequest request);

	/**
	 * ???????????????????????? ???????????????????????????
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return ??????????????????
	 * @author zhangweiwei
	 * @date 2021???3???9???,??????2:21:02
	 */
	@PostMapping("/db/v1/getMemberRedBagRecordsByMemberCards")
	ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecordsByMemberCards(
			@RequestBody ListAccountRequest request);

	@ApiOperation(value = "????????????????????? - ?????????")
	@PostMapping("db/v1/getMemberRedBagRecordsWithOutPage")
	ComResponse<List<MemberRedBagRecordsResponse>> getMemberRedBagRecordsWithOutPage(
			@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "????????????????????????")
	@PostMapping("db/v1/getMemberCoupon")
	ComResponse<Page<MemberCouponResponse>> getMemberCoupon(@RequestBody AccountRequest request);

	/**
	 * ???????????????????????? ??????????????????????????????
	 * 
	 * @param request {@link ListAccountRequest}
	 * @return ?????????????????????
	 * @author zhangweiwei
	 * @date 2021???3???9???,??????2:11:48
	 */
	@PostMapping("/db/v1/getMemberCouponByMemberCards")
	ComResponse<Page<MemberCouponResponse>> getMemberCouponByMemberCards(@RequestBody ListAccountRequest request);

	@ApiOperation(value = "???????????????????????? - ?????????")
	@PostMapping("db/v1/getMemberCouponWithOutPage")
	ComResponse<List<MemberCouponResponse>> getMemberCouponWithOutPage(@RequestBody AccountWithOutPageRequest request);

	@ApiOperation(value = "?????????????????????????????? ???????????????????????? ?????? ??????")
	@GetMapping("db/v1/getAccountByMemberCard")
	ComResponse<MemberAccountResponse> getAccountByMemberCard(@RequestParam("memberCard") String memberCard);

	@ApiOperation(value = "?????????????????????????????? ???????????????????????? ?????? ?????????????????????")
	@PostMapping("db/v1/getAccountHistoryByMemberCard")
	ComResponse<Page<MemberAccountHistoryResponse>> getAccountHistoryByMemberCard(
			@RequestBody AccountHistoryRequest request);

	@ApiOperation(value = "?????????????????????????????? ?????????????????????????????????????????????")
	@PostMapping("db/v1/getProductDiscountByProductCodes")
	ComResponse<ProductListDiscountResponse> getProductListDiscount(@RequestBody ProductListDiscountRequest request);

	@ApiOperation(value = "???????????????????????? ?????????????????????????????????????????????")
	@PostMapping("db/v1/getProductDiscountByProductCode")
	ComResponse<ProductDiscountResponse> getProductDiscount(@RequestBody ProductDiscountRequest request);

	@ApiOperation(value = "????????????")
	@PostMapping("db/v1/calculate")
	ComResponse<ProductPriceResponse> calculate(@RequestBody CalculateRequest request);

	/**
	 * ?????????????????????
	 * 
	 * @param request {@link CalculateOrderRequest}
	 * @return {@link CalculationOrderResponse}
	 * @author zhangweiwei
	 * @date 2021???3???15???,??????4:48:46
	 */
	@PostMapping("db/v1/calculateOrder")
	ComResponse<CalculationOrderResponse> calculateOrder(@RequestBody CalculateOrderRequest request);

	@ApiOperation(value = "??????????????????")
	@PostMapping("db/v1/checkOrderAmount")
	ComResponse<CalculationOrderResponse> checkOrderAmount(@RequestBody CheckOrderAmountRequest request);

	@GetMapping("db/v1/launchManage/getAllLaunchManage")
	ComResponse<List<LaunchManageDto>> getAllLaunchManage();

	@GetMapping("db/v1/launchManage/getLaunchManageByRelationBusNo")
	ComResponse<List<LaunchManageDto>> getLaunchManageByRelationBusNo(
			@RequestParam("relationBusNo") Long relationBusNo);

	@ApiOperation(value = "??????????????????-?????????????????????")
	@GetMapping("db/v1/productSales/getActivityList")
	ComResponse<List<ActivityDetailResponse>> getActivityList();

	@ApiOperation(value = "????????????-??????????????????-????????????????????????")
	@PostMapping("db/v1/memberLevelManager/getMemberLevelPages")
	ComResponse<Page<MemberLevelResponse>> getMemberLevelPages(@RequestBody PageModel request);

	@ApiOperation(value = "???????????????Id ????????????")
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
			logger.error("???????????????Id ???????????????", e);
		}
		return null;
	}

	/**
	 * ????????????- ??????????????????????????????
	 * 
	 * @param busNo ????????????
	 * @return ??????
	 * @author zhangweiwei
	 * @date 2021???2???20???,??????11:37:35
	 */
	@GetMapping("/db/v1/launchManage/getLaunchManageByBusNo")
	ComResponse<LaunchManageDto> getLaunchManageByBusNo(@RequestParam Integer busNo);

	/**
	 * ?????????????????????????????????
	 * 
	 * @param request {@link OrderSubmitRequest}
	 * @return {@link OrderSubmitResponse}
	 * @author zhangweiwei
	 * @date 2021???2???21???,??????5:08:19
	 */
	@PostMapping("/db/v1/orderSubmit")
	ComResponse<OrderSubmitResponse> orderSubmit(@RequestBody OrderSubmitRequest request);

	/**
	 * ?????????????????????????????????
	 *
	 * @param memberCards
	 * @return {@link OrderSubmitResponse}
	 * @author zhangweiwei
	 * @date 2021???2???21???,??????5:08:19
	 */
	@PostMapping("/db/v1/getAccountByMemberCards")
	ComResponse<List<MemberCouponInfoDTO>> getAccountByMemberCards(@RequestBody List<String> memberCards);

	@ApiOperation(value = "??????????????????????????????????????????")
	@GetMapping("db/v1/memberRedBag/getDtoByMemberCard")
	ComResponse<MemberRedBagDto> getDtoByMemberCard(@RequestParam("memberCard") String memberCard);
}

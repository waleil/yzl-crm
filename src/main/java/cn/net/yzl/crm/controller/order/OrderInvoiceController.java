package cn.net.yzl.crm.controller.order;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import cn.net.yzl.activity.model.requestModel.AccountRequest;
import cn.net.yzl.activity.model.requestModel.AccountWithOutPageRequest;
import cn.net.yzl.activity.model.requestModel.ListAccountRequest;
import cn.net.yzl.activity.model.responseModel.MemberCouponResponse;
import cn.net.yzl.activity.model.responseModel.MemberIntegralRecordsResponse;
import cn.net.yzl.activity.model.responseModel.MemberRedBagRecordsResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.client.order.ComparisonMgtFeignClient;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.constant.DmcActivityStatus;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.dto.order.MemberCouponDTO;
import cn.net.yzl.crm.dto.order.MemberCouponExportDTO;
import cn.net.yzl.crm.dto.order.MemberIntegralRecordsDTO;
import cn.net.yzl.crm.dto.order.MemberIntegralRecordsExportDTO;
import cn.net.yzl.crm.dto.order.MemberRedBagRecordsDTO;
import cn.net.yzl.crm.dto.order.MemberRedBagRecordsExportDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.ActivityService;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.BeanCopyUtils;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.order.model.vo.order.OrderInvoiceDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import cn.net.yzl.order.model.vo.order.SettlementDetailDistinctListDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orderInvoice")
@Api(tags = "结算中心")
@Slf4j
public class OrderInvoiceController {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");

	@Resource
	private OrderInvoiceClient orderInvoiceClient;

	@Resource
	private OrderInvoiceService orderInvoiceService;
	@Resource
	private EhrStaffClient ehrStaffClient;

	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityClient activityClient;
	@Autowired
	private SettlementFein settlementFein;
	@Autowired
	private MemberFien memberFien;
	@Resource
	private OrderFeignClient orderFeignClient;
	@Resource
	private ComparisonMgtFeignClient comparisonMgtFeignClient;

	@ApiOperation("查询订单发票申请列表")
	@PostMapping("v1/selectInvoiceApplyOrderList")
	public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceApplyOrderList(@RequestBody OrderInvoiceReqDTO dto) {
		return this.orderInvoiceClient.selectInvoiceApplyOrderList(dto);
	}

	@ApiOperation("根据订单号，查询剩余可开发票金额")
	@GetMapping("v1/selectRemainedAmt")
	public ComResponse<String> selectRemainedAmt(
			@RequestParam("orderNo") @NotNull(message = "订单编号不能为空") @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo) {
		return this.orderInvoiceClient.selectRemainedAmt(orderNo);
	}

	@ApiOperation("申请开票")
	@PostMapping("v1/applyInvoice")
	public ComResponse<Boolean> applyInvoice(@RequestBody OrderInvoiceDTO dto) {
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			throw new BizException(sresponse.getCode(), sresponse.getMessage());
		}
		dto.setOprCode(sresponse.getData().getStaffNo());
		dto.setOprName(sresponse.getData().getName());
		dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
		return this.orderInvoiceClient.applyInvoice(dto);
	}

	@ApiOperation("标记")
	@PostMapping("v1/tagInvoice")
	public ComResponse<Boolean> tagInvoice(@RequestBody OrderInvoiceDTO dto) {
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			throw new BizException(sresponse.getCode(), sresponse.getMessage());
		}
		dto.setOprCode(sresponse.getData().getStaffNo());
		dto.setOprName(sresponse.getData().getName());
		dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
		return this.orderInvoiceClient.tagInvoice(dto);
	}

	@ApiOperation("查询订单发票列表")
	@PostMapping("v1/selectInvoiceList")
	public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceList(@RequestBody OrderInvoiceReqDTO dto) {
		return this.orderInvoiceClient.selectInvoiceList(dto);
	}

	@ApiOperation(value = "顾客积分明细表")
	@PostMapping("/v1/getMemberIntegralRecords")
	public ComResponse<Page<MemberIntegralRecordsDTO>> getMemberIntegralRecords(@RequestBody AccountRequest request) {
		ComResponse<Page<MemberIntegralRecordsResponse>> response = null;
		if (StringUtils.hasText(request.getMemberCard())) {
			// 用正则表达式判断输入的参数是顾客姓名还是顾客卡号
			if (NUMBER_PATTERN.matcher(request.getMemberCard()).matches()) {
				// 如果是顾客卡号
				response = activityService.getMemberIntegralRecords(request);
			} else {
				// 如果是顾客姓名，用顾客姓名查询顾客卡号
				ComResponse<List<String>> queryMemberCards = this.memberFien.queryMemberCards(request.getMemberCard());
				if (Integer.compare(queryMemberCards.getStatus(), ComResponse.ERROR_STATUS) == 0) {
					log.error("按顾客姓名查询顾客卡号列表异常>>>{}", queryMemberCards);
					return ComResponse.fail(ResponseCodeEnums.ERROR,
							String.format("调用顾客姓名查询顾客卡号列表接口异常：%s", queryMemberCards.getMessage()));
				}
				if (CollectionUtils.isEmpty(queryMemberCards.getData())) {
					return ComResponse
							.success(AssemblerResultUtil
									.resultAssembler(Collections.<MemberIntegralRecordsDTO>emptyList()))
							.setMessage("调用顾客姓名查询顾客卡号列表接口：没有查询到数据。");
				}
				ListAccountRequest listRequest = new ListAccountRequest();
				listRequest.setMemberCards(queryMemberCards.getData());
				listRequest.setPageNo(request.getPageNo());
				listRequest.setPageSize(request.getPageSize());
				response = this.activityClient.getMemberIntegralRecordsByMemberCards(listRequest);
			}
		} else {
			response = activityService.getMemberIntegralRecords(request);
		}
		if (Integer.compare(response.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("顾客积分明细表异常>>>{}", response);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用DMC接口异常：%s", response.getMessage()));
		}
		Page<MemberIntegralRecordsResponse> responseData = response.getData();
		if (responseData.getItems().isEmpty()) {
			return ComResponse
					.success(AssemblerResultUtil.resultAssembler(Collections.<MemberIntegralRecordsDTO>emptyList()));
		}
		List<String> orderNoList = responseData.getItems().stream().map(MemberIntegralRecordsResponse::getOrderNo)
				.distinct().collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<Map<String, String>> queryFinancialNames = this.orderFeignClient.queryFinancialNames(orderNoList);
		if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单财务名称集合异常：%s", queryFinancialNames.getMessage()));
		}
		ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
				.querySettlementtimes(orderNoList);
		if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单对账时间集合异常：%s", querySettlementtimes.getMessage()));
		}
		Map<String, String> financialNames = queryFinancialNames.getData();
		Map<String, Date> settlementtimes = querySettlementtimes.getData();
		Page<MemberIntegralRecordsDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(responseData.getItems().stream().map(item -> {
			MemberIntegralRecordsDTO dto = BeanCopyUtils.transfer(item, MemberIntegralRecordsDTO.class);
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			dto.setFinancialOwnerName(Optional.ofNullable(financialNames.get(item.getOrderNo())).orElse("-"));
			dto.setMemberName(getMemberName(item.getMemberCard()));
			return dto;
		}).collect(Collectors.toList()));
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客积分明细表——导出")
	@PostMapping("v1/exportMemberIntegralRecords")
	public void exportMemberIntegralRecords(@RequestBody AccountWithOutPageRequest request,
			HttpServletResponse response) throws Exception {
		formatParams(request);
		ComResponse<List<MemberIntegralRecordsResponse>> records = activityClient
				.getMemberIntegralRecordsWithOutPage(request);
		if (!records.getStatus().equals(1)) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC异常，" + records.getMessage());
		}
		List<MemberIntegralRecordsResponse> responseData = records.getData();
		if (!Optional.ofNullable(responseData).map(List::isEmpty).isPresent()) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC未查询出数据");
		}
		List<String> orderNoList = responseData.stream().map(MemberIntegralRecordsResponse::getOrderNo).distinct()
				.collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein
				.getSettlementDetailGroupByOrderNo(orderNoList);
		List<SettlementDetailDistinctListDTO> data = dtos.getData();
		Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
		if (data != null) {
			collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
		}
		// 组装数据
		List<MemberIntegralRecordsExportDTO> list = new ArrayList<>();
		for (MemberIntegralRecordsResponse item : responseData) {
			MemberIntegralRecordsExportDTO dto = BeanCopyUtils.transfer(item, MemberIntegralRecordsExportDTO.class);
			dto.setStatusName(DmcActivityStatus.getName(item.getStatus()));
			List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
			if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
				dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
				dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
				dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
			}
			list.add(dto);
		}
		String title = "顾客积分明细表";
		this.export(list, MemberIntegralRecordsExportDTO.class, title, response);
	}

	@ApiOperation(value = "顾客红包明细表")
	@PostMapping("v1/getMemberRedBagRecords")
	public ComResponse<Page<MemberRedBagRecordsDTO>> getMemberRedBagRecords(@RequestBody AccountRequest request) {
		ComResponse<Page<MemberRedBagRecordsResponse>> response = null;
		if (StringUtils.hasText(request.getMemberCard())) {
			// 用正则表达式判断输入的参数是顾客姓名还是顾客卡号
			if (NUMBER_PATTERN.matcher(request.getMemberCard()).matches()) {
				// 如果是顾客卡号
				response = activityService.getMemberRedBagRecords(request);
			} else {
				// 如果是顾客姓名，用顾客姓名查询顾客卡号
				ComResponse<List<String>> queryMemberCards = this.memberFien.queryMemberCards(request.getMemberCard());
				if (Integer.compare(queryMemberCards.getStatus(), ComResponse.ERROR_STATUS) == 0) {
					log.error("按顾客姓名查询顾客卡号列表异常>>>{}", queryMemberCards);
					return ComResponse.fail(ResponseCodeEnums.ERROR,
							String.format("调用顾客姓名查询顾客卡号列表接口异常：%s", queryMemberCards.getMessage()));
				}
				if (CollectionUtils.isEmpty(queryMemberCards.getData())) {
					return ComResponse
							.success(AssemblerResultUtil
									.resultAssembler(Collections.<MemberRedBagRecordsDTO>emptyList()))
							.setMessage("调用顾客姓名查询顾客卡号列表接口：没有查询到数据。");
				}
				ListAccountRequest listRequest = new ListAccountRequest();
				listRequest.setMemberCards(queryMemberCards.getData());
				listRequest.setPageNo(request.getPageNo());
				listRequest.setPageSize(request.getPageSize());
				response = this.activityClient.getMemberRedBagRecordsByMemberCards(listRequest);
			}
		} else {
			response = activityService.getMemberRedBagRecords(request);
		}
		if (Integer.compare(response.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("顾客红包明细表异常>>>{}", response);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用DMC接口异常：%s", response.getMessage()));
		}
		Page<MemberRedBagRecordsResponse> responseData = response.getData();
		if (responseData.getItems().isEmpty()) {
			return ComResponse
					.success(AssemblerResultUtil.resultAssembler(Collections.<MemberRedBagRecordsDTO>emptyList()));
		}
		List<String> orderNoList = responseData.getItems().stream().map(MemberRedBagRecordsResponse::getOrderNo)
				.distinct().collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<Map<String, String>> queryFinancialNames = this.orderFeignClient.queryFinancialNames(orderNoList);
		if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单财务名称集合异常：%s", queryFinancialNames.getMessage()));
		}
		ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
				.querySettlementtimes(orderNoList);
		if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单对账时间集合异常：%s", querySettlementtimes.getMessage()));
		}
		Map<String, String> financialNames = queryFinancialNames.getData();
		Map<String, Date> settlementtimes = querySettlementtimes.getData();
		Page<MemberRedBagRecordsDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(responseData.getItems().stream().map(item -> {
			MemberRedBagRecordsDTO dto = BeanCopyUtils.transfer(item, MemberRedBagRecordsDTO.class);
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			dto.setFinancialOwnerName(Optional.ofNullable(financialNames.get(item.getOrderNo())).orElse("-"));
			dto.setMemberName(getMemberName(item.getMemberCard()));
			return dto;
		}).collect(Collectors.toList()));
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客红包明细表——导出")
	@PostMapping("v1/exportMemberRedBagRecords")
	public void exportMemberRedBagRecords(@RequestBody AccountWithOutPageRequest request, HttpServletResponse response)
			throws Exception {
		formatParams(request);
		ComResponse<List<MemberRedBagRecordsResponse>> records = activityClient
				.getMemberRedBagRecordsWithOutPage(request);
		if (!records.getStatus().equals(1)) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC异常，" + records.getMessage());
		}
		List<MemberRedBagRecordsResponse> responseData = records.getData();
		if (!Optional.ofNullable(responseData).map(List::isEmpty).isPresent()) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC未查询出数据");
		}
		List<String> orderNoList = responseData.stream().map(MemberRedBagRecordsResponse::getOrderNo).distinct()
				.collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein
				.getSettlementDetailGroupByOrderNo(orderNoList);
		List<SettlementDetailDistinctListDTO> data = dtos.getData();
		Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
		if (data != null) {
			collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
		}
		// 组装数据
		List<MemberRedBagRecordsExportDTO> list = new ArrayList<>();
		for (MemberRedBagRecordsResponse item : responseData) {
			MemberRedBagRecordsExportDTO dto = BeanCopyUtils.transfer(item, MemberRedBagRecordsExportDTO.class);
			dto.setStatusName(DmcActivityStatus.getName(item.getStatus()));
			List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
			if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
				dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
				dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
				dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
			}
			list.add(dto);
		}
		String title = "顾客红包明细表";
		this.export(list, MemberRedBagRecordsExportDTO.class, title, response);
	}

	/**
	 * TODO 获取顾客信息，测试完后就没用了
	 *
	 * @param memberCardNo
	 * @return
	 */
	public String getMemberName(String memberCardNo) {
		GeneralResult<Member> member = memberFien.getMember(memberCardNo);
		if (!member.getCode().equals(200)) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "顾客服务异常，" + member.getMessage());
		}
		return Optional.ofNullable(member.getData()).map(Member::getMember_name).orElse("");
	}

	@ApiOperation(value = "顾客优惠券明细表")
	@PostMapping("v1/getMemberCoupon")
	public ComResponse<Page<MemberCouponDTO>> getMemberCoupon(@RequestBody AccountRequest request) {
		ComResponse<Page<MemberCouponResponse>> response = null;
		if (StringUtils.hasText(request.getMemberCard())) {
			// 用正则表达式判断输入的参数是顾客姓名还是顾客卡号
			if (NUMBER_PATTERN.matcher(request.getMemberCard()).matches()) {
				// 如果是顾客卡号
				response = activityService.getMemberCoupon(request);
			} else {
				// 如果是顾客姓名，用顾客姓名查询顾客卡号
				ComResponse<List<String>> queryMemberCards = this.memberFien.queryMemberCards(request.getMemberCard());
				if (Integer.compare(queryMemberCards.getStatus(), ComResponse.ERROR_STATUS) == 0) {
					log.error("按顾客姓名查询顾客卡号列表异常>>>{}", queryMemberCards);
					return ComResponse.fail(ResponseCodeEnums.ERROR,
							String.format("调用顾客姓名查询顾客卡号列表接口异常：%s", queryMemberCards.getMessage()));
				}
				if (CollectionUtils.isEmpty(queryMemberCards.getData())) {
					return ComResponse
							.success(AssemblerResultUtil.resultAssembler(Collections.<MemberCouponDTO>emptyList()))
							.setMessage("调用顾客姓名查询顾客卡号列表接口：没有查询到数据。");
				}
				ListAccountRequest listRequest = new ListAccountRequest();
				listRequest.setMemberCards(queryMemberCards.getData());
				listRequest.setPageNo(request.getPageNo());
				listRequest.setPageSize(request.getPageSize());
				response = this.activityClient.getMemberCouponByMemberCards(listRequest);
			}
		} else {
			response = activityService.getMemberCoupon(request);
		}
		if (Integer.compare(response.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("顾客优惠券明细表异常>>>{}", response);
			return ComResponse.fail(ResponseCodeEnums.ERROR, String.format("调用DMC接口异常：%s", response.getMessage()));
		}
		Page<MemberCouponResponse> responseData = response.getData();
		if (responseData.getItems().isEmpty()) {
			return ComResponse.success(AssemblerResultUtil.resultAssembler(Collections.<MemberCouponDTO>emptyList()));
		}
		List<String> orderNoList = responseData.getItems().stream().map(MemberCouponResponse::getOrderNo).distinct()
				.collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<Map<String, String>> queryFinancialNames = this.orderFeignClient.queryFinancialNames(orderNoList);
		if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单财务名称集合异常：%s", queryFinancialNames.getMessage()));
		}
		ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
				.querySettlementtimes(orderNoList);
		if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
			return ComResponse.fail(ResponseCodeEnums.ERROR,
					String.format("调用订单对账时间集合异常：%s", querySettlementtimes.getMessage()));
		}
		Map<String, String> financialNames = queryFinancialNames.getData();
		Map<String, Date> settlementtimes = querySettlementtimes.getData();
		Page<MemberCouponDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(responseData.getItems().stream().map(item -> {
			MemberCouponDTO dto = BeanCopyUtils.transfer(item, MemberCouponDTO.class);
			if (!item.getCouponDiscountRulesDto().isEmpty()) {
				dto.setReduceAmount(item.getCouponDiscountRulesDto().get(0).getConditionFullD());
				dto.setCouponBusNo(item.getCouponDiscountRulesDto().get(0).getCouponBusNo());
			}
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			dto.setFinancialOwnerName(Optional.ofNullable(financialNames.get(item.getOrderNo())).orElse("-"));
			dto.setMemberName(getMemberName(item.getMemberCard()));
			return dto;
		}).collect(Collectors.toList()));
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客优惠券明细表——导出")
	@PostMapping("v1/exportMemberCoupon")
	public void exportMemberCoupon(@RequestBody AccountWithOutPageRequest request, HttpServletResponse response)
			throws Exception {
		formatParams(request);
		ComResponse<List<MemberCouponResponse>> records = activityClient.getMemberCouponWithOutPage(request);
		if (!records.getStatus().equals(1)) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC异常，" + records.getMessage());
		}
		List<MemberCouponResponse> responseData = records.getData();
		if (!Optional.ofNullable(responseData).map(List::isEmpty).isPresent()) {
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "DMC未查询出数据");
		}
		List<String> orderNoList = responseData.stream().map(MemberCouponResponse::getOrderNo).distinct()
				.collect(Collectors.toList());
		// 查询订单服务积分数据
		ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein
				.getSettlementDetailGroupByOrderNo(orderNoList);
		List<SettlementDetailDistinctListDTO> data = dtos.getData();
		Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
		if (data != null) {
			collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
		}
		// 组装数据
		List<MemberCouponExportDTO> list = new ArrayList<>();
		for (MemberCouponResponse item : responseData) {
			MemberCouponExportDTO dto = BeanCopyUtils.transfer(item, MemberCouponExportDTO.class);
			if (item.getCouponDiscountRulesDto().size() > 0) {
				dto.setReduceAmount(
						BigDecimal.valueOf(item.getCouponDiscountRulesDto().get(0).getReduceAmount() / 100));
				dto.setCouponBusNo(item.getCouponDiscountRulesDto().get(0).getCouponBusNo());
			}
			dto.setStatusName(DmcActivityStatus.getName(item.getStatus()));
			List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
			if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
				dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
				dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
				dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
			}
			list.add(dto);
		}
		String title = "顾客优惠券明细表";
		this.export(list, MemberCouponExportDTO.class, title, response);
	}

	private void formatParams(AccountWithOutPageRequest request) {
		// 若不传起止时间，则默认一年的时间范围
		if (null == request.getBeginTime() && null == request.getEndTime()) {
			Calendar c = Calendar.getInstance();
			Date thisDate = new Date();
			c.setTime(thisDate);
			c.add(Calendar.YEAR, -1);
			Date y = c.getTime();
			request.setBeginTime(y);
			request.setEndTime(thisDate);
		}
	}

	/**
	 * 导出
	 *
	 * @param list
	 * @param clazz
	 * @param title
	 * @param response
	 * @param <T>
	 */
	private <T> void export(List<?> list, Class<T> clazz, String title, HttpServletResponse response) throws Exception {
		// 导出
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
				URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
					.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
					.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			excelWriter.write(list, writeSheet);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "报表导出异常，请稍后再试");
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

}
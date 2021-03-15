package cn.net.yzl.crm.controller.order;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
import cn.net.yzl.activity.model.requestModel.ListAccountRequest;
import cn.net.yzl.activity.model.responseModel.MemberCouponResponse;
import cn.net.yzl.activity.model.responseModel.MemberIntegralRecordsResponse;
import cn.net.yzl.activity.model.responseModel.MemberRedBagRecordsResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.client.order.ComparisonMgtFeignClient;
import cn.net.yzl.crm.client.order.OrderFeignClient;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.config.QueryIds;
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
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderInvoiceDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
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
		Map<String, OrderM> financialNames = Collections.emptyMap();
		CompletableFuture<Map<String, OrderM>> cf1 = CompletableFuture.supplyAsync(() -> {
			// 查询订单服务积分数据
			ComResponse<Map<String, OrderM>> queryFinancialNames = this.orderFeignClient
					.queryFinancialNames(orderNoList);
			if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
				return Collections.emptyMap();
			}
			return queryFinancialNames.getData();
		});
		Map<String, Date> settlementtimes = Collections.emptyMap();
		CompletableFuture<Map<String, Date>> cf2 = CompletableFuture.supplyAsync(() -> {
			ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
					.querySettlementtimes(orderNoList);
			if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
				return Collections.emptyMap();
			}
			return querySettlementtimes.getData();
		});
		CompletableFuture.allOf(cf1, cf2);
		try {
			financialNames = cf1.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		try {
			settlementtimes = cf2.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		Page<MemberIntegralRecordsDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(new ArrayList<>());
		List<MemberIntegralRecordsResponse> items = responseData.getItems();
		for (MemberIntegralRecordsResponse item : items) {
			MemberIntegralRecordsDTO dto = new MemberIntegralRecordsDTO();
			dto.setCreateTime(item.getCreateTime());
			dto.setIntegral(item.getIntegral());
			dto.setMemberCard(item.getMemberCard());
			dto.setOperationType(item.getOperationType());
			dto.setOrderNo(item.getOrderNo());
			dto.setStatus(item.getStatus());
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			OrderM order = financialNames.get(item.getOrderNo());
			dto.setFinancialOwnerName(Optional.ofNullable(order).map(OrderM::getFinancialOwnerName).orElse("-"));
			dto.setMemberName(Optional.ofNullable(order).map(OrderM::getMemberName).orElse("-"));
			page.getItems().add(dto);
		}
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客积分明细表——导出")
	@PostMapping("v1/exportMemberIntegralRecords")
	public void exportMemberIntegralRecords(@RequestBody AccountRequest request, HttpServletResponse response)
			throws Exception {
		this.formatParams(request);
		request.setPageNo(1);// 默认第1页
		request.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<MemberIntegralRecordsDTO>> data = this.getMemberIntegralRecords(request);
		if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客积分明细表异常");
		}
		Page<MemberIntegralRecordsDTO> page = data.getData();
		PageParam param = page.getPageParam();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "顾客积分明细表";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				String.format("attachment;filename=%s.xlsx", URLEncoder.encode(title, StandardCharsets.UTF_8.name())));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), MemberIntegralRecordsExportDTO.class)
					.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy())
					.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy()).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					request.setPageNo(i);
					data = this.getMemberIntegralRecords(request);
					if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
						throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客积分明细表异常");
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
					page.getItems().clear();// 存储完成后清理集合
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
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
		Map<String, OrderM> financialNames = Collections.emptyMap();
		CompletableFuture<Map<String, OrderM>> cf1 = CompletableFuture.supplyAsync(() -> {
			// 查询订单服务积分数据
			ComResponse<Map<String, OrderM>> queryFinancialNames = this.orderFeignClient
					.queryFinancialNames(orderNoList);
			if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
				return Collections.emptyMap();
			}
			return queryFinancialNames.getData();
		});
		Map<String, Date> settlementtimes = Collections.emptyMap();
		CompletableFuture<Map<String, Date>> cf2 = CompletableFuture.supplyAsync(() -> {
			ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
					.querySettlementtimes(orderNoList);
			if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
				return Collections.emptyMap();
			}
			return querySettlementtimes.getData();
		});
		CompletableFuture.allOf(cf1, cf2);
		try {
			financialNames = cf1.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		try {
			settlementtimes = cf2.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		Page<MemberRedBagRecordsDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(new ArrayList<>());
		List<MemberRedBagRecordsResponse> items = responseData.getItems();
		for (MemberRedBagRecordsResponse item : items) {
			MemberRedBagRecordsDTO dto = new MemberRedBagRecordsDTO();
			Optional.ofNullable(item.getAmount()).ifPresent(c -> dto.setAmount(BigDecimal.valueOf(c)));
			dto.setCreateTime(item.getCreateTime());
			dto.setMemberCard(item.getMemberCard());
			dto.setOperationType(item.getOperationType());
			dto.setOrderNo(item.getOrderNo());
			dto.setStatus(item.getStatus());
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			OrderM order = financialNames.get(item.getOrderNo());
			dto.setFinancialOwnerName(Optional.ofNullable(order).map(OrderM::getFinancialOwnerName).orElse("-"));
			dto.setMemberName(Optional.ofNullable(order).map(OrderM::getMemberName).orElse("-"));
			page.getItems().add(dto);
		}
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客红包明细表——导出")
	@PostMapping("v1/exportMemberRedBagRecords")
	public void exportMemberRedBagRecords(@RequestBody AccountRequest request, HttpServletResponse response)
			throws Exception {
		this.formatParams(request);
		request.setPageNo(1);// 默认第1页
		request.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<MemberRedBagRecordsDTO>> data = this.getMemberRedBagRecords(request);
		if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客红包明细表异常");
		}
		Page<MemberRedBagRecordsDTO> page = data.getData();
		PageParam param = page.getPageParam();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "顾客红包明细表";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				String.format("attachment;filename=%s.xlsx", URLEncoder.encode(title, StandardCharsets.UTF_8.name())));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), MemberRedBagRecordsExportDTO.class)
					.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy())
					.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy()).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					request.setPageNo(i);
					data = this.getMemberRedBagRecords(request);
					if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
						throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客红包明细表异常");
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
					page.getItems().clear();// 存储完成后清理集合
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
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
		Map<String, OrderM> financialNames = Collections.emptyMap();
		CompletableFuture<Map<String, OrderM>> cf1 = CompletableFuture.supplyAsync(() -> {
			// 查询订单服务积分数据
			ComResponse<Map<String, OrderM>> queryFinancialNames = this.orderFeignClient
					.queryFinancialNames(orderNoList);
			if (Integer.compare(queryFinancialNames.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单财务名称集合异常>>>{}", queryFinancialNames);
				return Collections.emptyMap();
			}
			return queryFinancialNames.getData();
		});
		Map<String, Date> settlementtimes = Collections.emptyMap();
		CompletableFuture<Map<String, Date>> cf2 = CompletableFuture.supplyAsync(() -> {
			ComResponse<Map<String, Date>> querySettlementtimes = this.comparisonMgtFeignClient
					.querySettlementtimes(orderNoList);
			if (Integer.compare(querySettlementtimes.getStatus(), ComResponse.ERROR_STATUS) == 0) {
				log.error("订单对账时间集合异常>>>{}", querySettlementtimes);
				return Collections.emptyMap();
			}
			return querySettlementtimes.getData();
		});
		CompletableFuture.allOf(cf1, cf2);
		try {
			financialNames = cf1.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		try {
			settlementtimes = cf2.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		Page<MemberCouponDTO> page = new Page<>();
		page.setPageParam(responseData.getPageParam());
		page.setItems(new ArrayList<>());
		List<MemberCouponResponse> items = responseData.getItems();
		for (MemberCouponResponse item : items) {
			MemberCouponDTO dto = new MemberCouponDTO();
			dto.setCreateTime(item.getCreateTime());
			dto.setMemberCard(item.getMemberCard());
			dto.setOrderNo(item.getOrderNo());
			dto.setStatus(item.getStatus());
			if (!item.getCouponDiscountRulesDto().isEmpty()) {
				dto.setReduceAmount(item.getCouponDiscountRulesDto().get(0).getConditionFullD());
				dto.setCouponBusNo(item.getCouponDiscountRulesDto().get(0).getCouponBusNo());
			}
			dto.setReconciliationTime(Optional.ofNullable(settlementtimes.get(item.getOrderNo())).orElse(null));
			OrderM order = financialNames.get(item.getOrderNo());
			dto.setFinancialOwnerName(Optional.ofNullable(order).map(OrderM::getFinancialOwnerName).orElse("-"));
			dto.setMemberName(Optional.ofNullable(order).map(OrderM::getMemberName).orElse("-"));
			page.getItems().add(dto);
		}
		return ComResponse.success(page);
	}

	@ApiOperation(value = "顾客优惠券明细表——导出")
	@PostMapping("v1/exportMemberCoupon")
	public void exportMemberCoupon(@RequestBody AccountRequest request, HttpServletResponse response) throws Exception {
		this.formatParams(request);
		request.setPageNo(1);// 默认第1页
		request.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<MemberCouponDTO>> data = this.getMemberCoupon(request);
		if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客优惠券明细表异常");
		}
		Page<MemberCouponDTO> page = data.getData();
		PageParam param = page.getPageParam();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "顾客优惠券明细表";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				String.format("attachment;filename=%s.xlsx", URLEncoder.encode(title, StandardCharsets.UTF_8.name())));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), MemberCouponExportDTO.class)
					.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy())
					.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy()).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					request.setPageNo(i);
					data = this.getMemberCoupon(request);
					if (Integer.compare(data.getStatus(), ComResponse.ERROR_STATUS) == 0) {
						throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出顾客优惠券明细表异常");
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
					page.getItems().clear();// 存储完成后清理集合
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	private void formatParams(AccountRequest request) {
		// 起止时间：当前时间所在的月份减去一个月
		if (null == request.getBeginTime() && null == request.getEndTime()) {
			Calendar c = Calendar.getInstance();
			Date thisDate = new Date();
			c.setTime(thisDate);
			c.add(Calendar.MONTH, -1);
			Date y = c.getTime();
			request.setBeginTime(y);
			request.setEndTime(thisDate);
		}
	}

}
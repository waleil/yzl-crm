package cn.net.yzl.crm.controller.order;

import cn.net.yzl.activity.model.requestModel.AccountRequest;
import cn.net.yzl.activity.model.requestModel.AccountWithOutPageRequest;
import cn.net.yzl.activity.model.responseModel.MemberCouponResponse;
import cn.net.yzl.activity.model.responseModel.MemberIntegralRecordsResponse;
import cn.net.yzl.activity.model.responseModel.MemberRedBagRecordsResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.order.MemberCouponDTO;
import cn.net.yzl.crm.dto.order.MemberIntegralRecordsDTO;
import cn.net.yzl.crm.dto.order.MemberRedBagRecordsDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.ActivityService;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.BeanCopyUtils;
import cn.net.yzl.order.model.vo.order.OrderInvoiceDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import cn.net.yzl.order.model.vo.order.SettlementDetailDistinctListDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("orderInvoice")
@Api(tags = "结算中心")
public class OrderInvoiceController {

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

    @ApiOperation("查询订单发票申请列表")
    @PostMapping("v1/selectInvoiceApplyOrderList")
    public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceApplyOrderList(@RequestBody OrderInvoiceReqDTO dto) {
        return this.orderInvoiceClient.selectInvoiceApplyOrderList(dto);
    }


    @ApiOperation("根据订单号，查询剩余可开发票金额")
    @GetMapping("v1/selectRemainedAmt")
    public ComResponse<String> selectRemainedAmt(@RequestParam("orderNo")
                                                 @NotNull(message = "订单编号不能为空")
                                                 @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo) {
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
    @PostMapping("v1/getMemberIntegralRecords")
    public ComResponse<Page<MemberIntegralRecordsDTO>> getMemberIntegralRecords(@RequestBody AccountRequest request) {
        ComResponse<Page<MemberIntegralRecordsResponse>> response = activityService.getMemberIntegralRecords(request);
        if (!response.getCode().equals(ResponseCodeEnums.SUCCESS_CODE.getCode())) {
            throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "EHR异常，" + response.getMessage());
        }
        Page<MemberIntegralRecordsResponse> responseData = response.getData();
        if (responseData.getItems().size() == 0) {
            return ComResponse.success();
        }
        List<String> orderNoList = responseData.getItems().stream().map(MemberIntegralRecordsResponse::getOrderNo).distinct().collect(Collectors.toList());
        //查询订单服务积分数据
        ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein.getSettlementDetailGroupByOrderNo(orderNoList);
        List<SettlementDetailDistinctListDTO> data = dtos.getData();
        Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
        if (data != null) {
            collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
        }

        Page<MemberIntegralRecordsDTO> page = new Page<>();
        page.setPageParam(responseData.getPageParam());
        List<MemberIntegralRecordsDTO> list = new ArrayList<>();
        for (MemberIntegralRecordsResponse item : responseData.getItems()) {
            MemberIntegralRecordsDTO dto = BeanCopyUtils.transfer(item, MemberIntegralRecordsDTO.class);
            List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
            if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
                dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
                dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
                dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
            }
            list.add(dto);
        }
        page.setItems(list);
        return ComResponse.success(page);
    }

    @ApiOperation(value = "顾客积分明细表")
    @PostMapping("v1/exportMemberIntegralRecords")
    public void exportMemberIntegralRecords(@RequestBody AccountWithOutPageRequest request, HttpServletResponse response) {
        ComResponse<List<MemberIntegralRecordsResponse>> records = activityClient.getMemberIntegralRecordsWithOutPage(request);
        if (!records.getCode().equals(ResponseCodeEnums.SUCCESS_CODE.getCode())) {
            throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "EHR异常，" + records.getMessage());
        }
//        Page<MemberIntegralRecordsResponse> responseData = records.getData();
//        if (responseData.getItems().size() == 0) {
//            return ComResponse.success();
//        }
//        List<String> orderNoList = responseData.getItems().stream().map(MemberIntegralRecordsResponse::getOrderNo).distinct().collect(Collectors.toList());
//        //查询订单服务积分数据
//        ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein.getSettlementDetailGroupByOrderNo(orderNoList);
//        List<SettlementDetailDistinctListDTO> data = dtos.getData();
//        Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
//        if (data != null) {
//            collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
//        }
//
//        Page<MemberIntegralRecordsDTO> page = new Page<>();
//        page.setPageParam(responseData.getPageParam());
//        List<MemberIntegralRecordsDTO> list = new ArrayList<>();
//        for (MemberIntegralRecordsResponse item : responseData.getItems()) {
//            MemberIntegralRecordsDTO dto = BeanCopyUtils.transfer(item, MemberIntegralRecordsDTO.class);
//            List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
//            if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
//                dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
//                dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
//                dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
//            }
//            list.add(dto);
//        }
//        page.setItems(list);
//        return ComResponse.success(page);
    }

    @ApiOperation(value = "顾客红包明细表")
    @PostMapping("v1/getMemberRedBagRecords")
    public ComResponse<Page<MemberRedBagRecordsDTO>> getMemberRedBagRecords(@RequestBody AccountRequest request) {
        ComResponse<Page<MemberRedBagRecordsResponse>> response = activityService.getMemberRedBagRecords(request);
        if (!response.getCode().equals(ResponseCodeEnums.SUCCESS_CODE.getCode())) {
            throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "EHR异常，" + response.getMessage());
        }
        Page<MemberRedBagRecordsResponse> responseData = response.getData();
        if (responseData.getItems().size() == 0) {
            return ComResponse.success();
        }
        List<String> orderNoList = responseData.getItems().stream().map(MemberRedBagRecordsResponse::getOrderNo).distinct().collect(Collectors.toList());
        //查询订单服务积分数据
        ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein.getSettlementDetailGroupByOrderNo(orderNoList);
        List<SettlementDetailDistinctListDTO> data = dtos.getData();
        Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
        if (data != null) {
            collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
        }

        Page<MemberRedBagRecordsDTO> page = new Page<>();
        page.setPageParam(responseData.getPageParam());
        List<MemberRedBagRecordsDTO> list = new ArrayList<>();
        for (MemberRedBagRecordsResponse item : responseData.getItems()) {
            MemberRedBagRecordsDTO dto = BeanCopyUtils.transfer(item, MemberRedBagRecordsDTO.class);
            List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
            if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
                dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
                dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
                dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
            }
            list.add(dto);
        }
        page.setItems(list);
        return ComResponse.success(page);
    }

    @ApiOperation(value = "顾客优惠券明细表")
    @PostMapping("v1/getMemberCoupon")
    public ComResponse<Page<MemberCouponDTO>> getMemberCoupon(@RequestBody AccountRequest request) {
        ComResponse<Page<MemberCouponResponse>> response = activityService.getMemberCoupon(request);
        if (!response.getCode().equals(ResponseCodeEnums.SUCCESS_CODE.getCode())) {
            throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "EHR异常，" + response.getMessage());
        }
        Page<MemberCouponResponse> responseData = response.getData();
        if (responseData.getItems().size() == 0) {
            return ComResponse.success();
        }
        List<String> orderNoList = responseData.getItems().stream().map(MemberCouponResponse::getOrderNo).distinct().collect(Collectors.toList());
        //查询订单服务积分数据
        ComResponse<List<SettlementDetailDistinctListDTO>> dtos = settlementFein.getSettlementDetailGroupByOrderNo(orderNoList);
        List<SettlementDetailDistinctListDTO> data = dtos.getData();
        Map<String, List<SettlementDetailDistinctListDTO>> collectMap = new HashMap<>();
        if (data != null) {
            collectMap = data.stream().collect(Collectors.groupingBy(SettlementDetailDistinctListDTO::getOrderNo));
        }

        Page<MemberCouponDTO> page = new Page<>();
        page.setPageParam(responseData.getPageParam());
        List<MemberCouponDTO> list = new ArrayList<>();
        for (MemberCouponResponse item : responseData.getItems()) {
            MemberCouponDTO dto = BeanCopyUtils.transfer(item, MemberCouponDTO.class);
            if (item.getCouponDiscountRulesDto().size() > 0){
                dto.setReduceAmount(item.getCouponDiscountRulesDto().get(0).getReduceAmount());
                dto.setCouponBusNo(item.getCouponDiscountRulesDto().get(0).getCouponBusNo());
            }
            List<SettlementDetailDistinctListDTO> settlementDetailDistinctListDTOS = collectMap.get(item.getOrderNo());
            if (Optional.ofNullable(settlementDetailDistinctListDTOS).map(List::isEmpty).isPresent()) {
                dto.setMemberName(settlementDetailDistinctListDTOS.get(0).getMemberName());
                dto.setReconciliationTime(settlementDetailDistinctListDTOS.get(0).getCreateTime());
                dto.setFinancialOwnerName(settlementDetailDistinctListDTOS.get(0).getFinancialOwnerName());
            }
            list.add(dto);
        }
        page.setItems(list);
        return ComResponse.success(page);
    }


}
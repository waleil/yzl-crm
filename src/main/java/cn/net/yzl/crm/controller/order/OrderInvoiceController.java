package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderInvoiceDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

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
    public ComResponse<Boolean> applyInvoice (@RequestBody OrderInvoiceDTO dto) {
        ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
            throw new BizException(sresponse.getCode(),sresponse.getMessage());
        }
        dto.setOprCode(sresponse.getData().getStaffNo());
        dto.setOprName(sresponse.getData().getName());
        dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
        return this.orderInvoiceClient.applyInvoice(dto);
    }


    @ApiOperation("标记")
    @PostMapping("v1/tagInvoice")
    public ComResponse<Boolean> tagInvoice (@RequestBody OrderInvoiceDTO dto ) {
        ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
            throw new BizException(sresponse.getCode(),sresponse.getMessage());
        }
        dto.setOprCode(sresponse.getData().getStaffNo());
        dto.setOprName(sresponse.getData().getName());
        dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
        return this.orderInvoiceClient.tagInvoice(dto);
    }


    @ApiOperation("查询订单发票列表")
    @PostMapping("v1/selectInvoiceList")
    public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceList (@RequestBody OrderInvoiceReqDTO dto ) {
        return this.orderInvoiceClient.selectInvoiceList(dto);
    }



}
package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.service.order.ExportOrderService;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

//import cn.net.yzl.crm.client.order.ExportOrderService;

/**
 * @author chengyu
 * @version 1.0
 * @date 2021/2/5 10:07
 */
@Controller
@Api(tags = "结算中心")
@RequestMapping("down")
public class ExportOrderController {


    @Resource
    private OrderInvoiceService orderInvoiceService;
    @Resource
    private SettlementFein settlementFein;

    @Resource
    private ExportOrderService service;

    @ApiOperation(value = "订单发票列表导出")
    @PostMapping("v1/exportInvoiceList")
    public ComResponse<Boolean> exportInvoiceList(@RequestBody  OrderInvoiceReqDTO dto, HttpServletResponse response) throws IOException {
        //为了保障系统性能稳定性，导出的最大时间限制时间为1个月，请重新选择查询范围。
        if (dto.getStartTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择开始时间");
        }
        if (dto.getEndTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择结束时间");
        }
        if (Duration
                .between(dto.getStartTime(),dto.getEndTime())
                .toDays() > 31L) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。");
        }

        orderInvoiceService.exportInvoiceList(dto,response);
        return ComResponse.success(true);
    }

    @PostMapping("v1/exportSettlementList")
    @ApiOperation("导出结算列表")
    public ComResponse<Boolean> exportSettlementList(@RequestBody SettlementListReqDTO dto, HttpServletResponse response) {
        //为了保障系统性能稳定性，导出的最大时间限制时间为1个月，请重新选择查询范围。
        if (dto.getStartCreateTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择开始时间");
        }
        if (dto.getEndCreateTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择结束时间");
        }
        if (Duration
                .between(LocalDateTime.ofInstant(dto.getStartCreateTime().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(dto.getEndCreateTime().toInstant(), ZoneId.systemDefault()))
                .toDays() > 31L) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。");
        }
        service.exportSettlementList(dto,response);
        return ComResponse.success(true);

    }

    @PostMapping("v1/exportselectProductDetailBySettledOrder")
    @ApiOperation("以商品为维度的结算商品列表导出")
    public ComResponse<Boolean> exportselectProductDetailBySettledOrder(@RequestBody ProductDetailSettlementedReqDTO dto, HttpServletResponse response){
        //为了保障系统性能稳定性，导出的最大时间限制时间为1个月，请重新选择查询范围。
        if (dto.getStartCreateTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择开始时间");
        }
        if (dto.getEndCreateTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择结束时间");
        }
        if (Duration
                .between(LocalDateTime.ofInstant(dto.getStartCreateTime().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(dto.getEndCreateTime().toInstant(), ZoneId.systemDefault()))
                .toDays() > 7L) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为7天，请重新选择查询范围。");
        }

        service.exportSelectProductDetailBySettledOrder(dto,response);
        return ComResponse.success(true);

    }

    @PostMapping("v1/exportselectgoodsInTransitlist")
    @ApiOperation("导出在途商品列表")
    public ComResponse<Boolean> exportselectgoodsInTransitlist(@RequestBody GoodsInTransitReqDTO dto, HttpServletResponse response){
        //为了保障系统性能稳定性，导出的最大时间限制时间为1个月，请重新选择查询范围。
        if (dto.getStartShippingtime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择开始时间");
        }
        if (dto.getStartShippingtime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择结束时间");
        }
        if (Duration
                .between(LocalDateTime.ofInstant(dto.getStartShippingtime().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(dto.getEndShippingtime().toInstant(), ZoneId.systemDefault()))
                .toDays() > 31L) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。");
        }
        service.exportSelectgoodsInTransitlist(dto,response);
        return ComResponse.success(true);

    }

    @ApiOperation(value = "查询订单销售明细")
    @PostMapping("v1/exportselectOrderSaleDetail")
    public void sexportselectOrderSaleDetail(@RequestBody OrderSellDetailReqDTO dto,HttpServletResponse response) {
        //为了保障系统性能稳定性，导出的最大时间限制时间为1个月，请重新选择查询范围。
        if (dto.getStartPayTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择开始时间");
        }
        if (dto.getEndPayTime() == null) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择结束时间");
        }
        if (Duration
                .between(LocalDateTime.ofInstant(dto.getStartPayTime().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(dto.getEndPayTime().toInstant(), ZoneId.systemDefault()))
                .toDays() > 31L) {
            throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。");
        }
      service.sexportselectOrderSaleDetail(dto,response);
    }


}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.service.order.ExportOrderService;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
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
import java.util.List;

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
        orderInvoiceService.exportInvoiceList(dto,response);
        return ComResponse.success(true);
    }

    @PostMapping("v1/exportSettlementList")
    @ApiOperation("导出结算列表")
    public ComResponse<Boolean> exportSettlementList(@RequestBody SettlementListReqDTO dto, HttpServletResponse response) {
        service.exportSettlementList(dto,response);
        return ComResponse.success(true);

    }

    @PostMapping("v1/exportselectProductDetailBySettledOrder")
    @ApiOperation("以商品为维度的结算商品列表导出")
    public ComResponse<Boolean> exportselectProductDetailBySettledOrder(@RequestBody ProductDetailSettlementedReqDTO dto, HttpServletResponse response){
        service.exportSelectProductDetailBySettledOrder(dto,response);
        return ComResponse.success(true);

    }

    @PostMapping("v1/exportselectgoodsInTransitlist")
    @ApiOperation("导出在途商品列表")
    public ComResponse<Boolean> exportselectgoodsInTransitlist(@RequestBody GoodsInTransitReqDTO dto, HttpServletResponse response){
        service.exportSelectgoodsInTransitlist(dto,response);
        return ComResponse.success(true);

    }


}

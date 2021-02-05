package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.client.store.InventoryFeginService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.vo.InventoryExcelVo;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import cn.net.yzl.model.vo.InventoryProductResultExcelVo;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ApiOperation(value = "订单发票列表导出")
    @PostMapping("v1/exportInvoiceList")
    public ComResponse<Boolean> exportInvoiceList(@RequestBody  OrderInvoiceReqDTO dto, HttpServletResponse response) throws IOException {
        orderInvoiceService.exportInvoiceList(dto,response);
        return ComResponse.success(true);
    }




}

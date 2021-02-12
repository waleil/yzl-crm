package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.model.order.OrderInvoice4Export;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.OrderLogisticsStatus;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    @Resource
    private OrderInvoiceClient orderInvoiceClient;


    @Override
    public void exportInvoiceList(OrderInvoiceReqDTO dto, HttpServletResponse response) throws IOException {
        ComResponse<Page<OrderInvoiceListDTO>> res = orderInvoiceClient.selectInvoiceList(dto);
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(response));
            return;
        }
        if(res.getData() == null || CollectionUtils.isEmpty(res.getData().getItems())){
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(res));
            return;
        }

        //系统时间
        String sysDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        response.setCharacterEncoding("UTF-8");
        //响应内容格式
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment;fileName=订单发票列表%s.xlsx", sysDate));
        List<OrderInvoice4Export> list = new ArrayList<>();
        res.getData().getItems().forEach(map ->{
            OrderInvoice4Export orderInvoice4Export = new OrderInvoice4Export();
            orderInvoice4Export.setOrderNo(map.getOrderNo());
            orderInvoice4Export.setFinancialOwnerName(map.getFinancialOwnerName());
            orderInvoice4Export.setTaxMode(CommonConstant.TAX_MODE_0 == map.getTaxMode()?"电子发票":"纸质发票");
            orderInvoice4Export.setStatsStr(CommonConstant.INVOICE_FLAG_T == map.getStats()?"已开票":"未开票");
            orderInvoice4Export.setPayType(CommonConstant.PAY_TYPE_0 == map.getPayType()?"货到付款":"款到发货");
            orderInvoice4Export.setLogisticsStatus(OrderLogisticsStatus.getName(map.getLogisticsStatus()));
            orderInvoice4Export.setReveiverAddress(map.getReveiverAddress());
            orderInvoice4Export.setCreateTime(map.getCreateTime());
            orderInvoice4Export.setInvoiceTime(map.getInvoiceTime());
            list.add(orderInvoice4Export);

        });
        //向前端写入文件流流
        EasyExcel.write(response.getOutputStream(), OrderInvoice4Export.class)
                .sheet("订单发票列表").doWrite(list);


    }



}

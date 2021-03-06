package cn.net.yzl.crm.service.impl.order;
import java.util.Date;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.OrderInvoiceClient;
import cn.net.yzl.crm.model.order.OrderInvoice4Export;
import cn.net.yzl.crm.service.order.OrderInvoiceService;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.OrderLogisticsStatus;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    @Resource
    private OrderInvoiceClient orderInvoiceClient;
    private WriteHandler writeHandler = new LongestMatchColumnWidthStyleStrategy();

    @Override
    public void exportInvoiceList(OrderInvoiceReqDTO dto, HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = null;
        try {
            dto.setPageNo(1);// 默认第1页
            dto.setPageSize(1000);// 默认每页1000条数据
            ComResponse<Page<OrderInvoiceListDTO>> res = orderInvoiceClient.selectInvoiceList(dto);
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
                log.error("导出订单发票列表异常>>>{}", res);
                return;
            }
            Page<OrderInvoiceListDTO> page = res.getData();
            PageParam param = page.getPageParam();
            if (param.getPageTotal() == 0) {
                log.info("订单发票列表为空>>>{}", param);

            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            String title = new String("订单发票列表".getBytes(),StandardCharsets.UTF_8.name());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
                    URLEncoder.encode(title, StandardCharsets.UTF_8.name()), DateFormatUtil.dateToString(new Date(),CommonConstant.JSON_FORMAT_PATTERN)));

            excelWriter = EasyExcel.write(response.getOutputStream(), OrderInvoice4Export.class)
                    .registerWriteHandler(this.writeHandler).build();
            // 写入到同一个sheet
            WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
            // 此处已经获取到第一页的数据
            List<OrderInvoice4Export> settlement4ExportDTOS = this.formatesettleData(page.getItems());
            excelWriter.write(settlement4ExportDTOS, writeSheet);
            settlement4ExportDTOS.clear();
            page.getItems().clear();// 存储完成后清理集合
            // 如果总页数大于1
            if (param.getPageTotal() > 1) {
                // 直接从第二页开始获取
                for (int i = 2; i <= param.getPageTotal(); i++) {
                    dto.setPageNo(i);
                    res = orderInvoiceClient.selectInvoiceList(dto);
                    if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
                        log.error("导出结算列表异常>>>{}", res);
                        return;
                    }
                    page = res.getData();
                    settlement4ExportDTOS = this.formatesettleData(page.getItems());
                    excelWriter.write(settlement4ExportDTOS, writeSheet);
                    settlement4ExportDTOS.clear();
                    page.getItems().clear();// 存储完成后清理集合
                }
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }


    private List<OrderInvoice4Export> formatesettleData(List<OrderInvoiceListDTO> items) {
        List<OrderInvoice4Export> list = new ArrayList<>();
        items.forEach(map ->{
            OrderInvoice4Export orderInvoice4Export = new OrderInvoice4Export();
            if(map.getTaxWay()!=null){
                orderInvoice4Export.setTaxWay(CommonConstant.TAX_WAY_0 ==map.getTaxWay()?"专票":"普票");
            }
            orderInvoice4Export.setTaxMoney(map.getTaxMoney());
            orderInvoice4Export.setOrderNo(map.getOrderNo());
            orderInvoice4Export.setFinancialOwnerName(map.getFinancialOwnerName());
            orderInvoice4Export.setTaxMode(CommonConstant.TAX_MODE_0 == map.getTaxMode()?"电子发票":"纸质发票");
            orderInvoice4Export.setStatsStr(CommonConstant.INVOICE_FLAG_T == map.getStats()?"已开票":"未开票");
            orderInvoice4Export.setPayType(CommonConstant.PAY_TYPE_0 == map.getPayType()?"货到付款":"款到发货");
            orderInvoice4Export.setLogisticsStatus(OrderLogisticsStatus.codeToName(map.getLogisticsStatus()));
            orderInvoice4Export.setReveiverAddress(map.getReveiverAddress());
            orderInvoice4Export.setCreateTime(map.getCreateTime());
            orderInvoice4Export.setInvoiceTime(map.getInvoiceTime());

            list.add(orderInvoice4Export);

        });
        return list;
    }


}

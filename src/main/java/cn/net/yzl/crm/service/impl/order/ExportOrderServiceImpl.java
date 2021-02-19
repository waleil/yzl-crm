package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.service.order.ExportOrderService;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.vo.order.CompareOrderType1Out;
import cn.net.yzl.order.model.vo.order.Settlement4ExportDTO;
import cn.net.yzl.order.model.vo.order.SettlementDTO;
import cn.net.yzl.order.model.vo.order.SettlementListReqDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExportOrderServiceImpl implements ExportOrderService {

    @Resource
    private SettlementFein settlementFein;
    private WriteHandler writeHandler = new LongestMatchColumnWidthStyleStrategy();
    @Override
    public void exportSettlementList(SettlementListReqDTO dto, HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        try {
        dto.setPageNo(1);// 默认第1页
        dto.setPageSize(1000);// 默认每页1000条数据
        ComResponse<Page<SettlementDTO>>  data = this.settlementFein.settlementList(dto);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
            log.error("导出结算列表异常>>>{}", data);
            return;
        }
        Page<SettlementDTO> page = data.getData();
        PageParam param = page.getPageParam();
        if (param.getPageTotal() == 0) {
            log.info("结算列表为空>>>{}", param);
            return;
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String title = "结算列表";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
                URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));


            excelWriter = EasyExcel.write(response.getOutputStream(), Settlement4ExportDTO.class)
                    .registerWriteHandler(this.writeHandler).build();
            // 写入到同一个sheet
            WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
            // 此处已经获取到第一页的数据
            List<Settlement4ExportDTO> settlement4ExportDTOS = this.formatesettleData(page.getItems());
            excelWriter.write(settlement4ExportDTOS, writeSheet);
            settlement4ExportDTOS.clear();
            page.getItems().clear();// 存储完成后清理集合
            // 如果总页数大于1
            if (param.getPageTotal() > 1) {
                // 直接从第二页开始获取
                for (int i = 2; i <= param.getPageTotal(); i++) {
                    dto.setPageNo(i);
                    data = this.settlementFein.settlementList(dto);
                    if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
                        log.error("导出结算列表异常>>>{}", data);
                        return;
                    }
                    page = data.getData();
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

    /**
     * 格式化返回结算订单数据
     * @param list
     * @return
     */
    private List<Settlement4ExportDTO> formatesettleData(List<SettlementDTO> list){
        List<Settlement4ExportDTO> result = new ArrayList<>();
        list.forEach(m->{
            Settlement4ExportDTO dto = new Settlement4ExportDTO();
            dto.setExpressCompanyName(m.getExpressCompanyName());
            dto.setExternalBillSn(m.getExternalBillSn());
            dto.setFinancialOwnerName(m.getFinancialOwnerName());
            dto.setFreezeTotalmoney(m.getFreezeTotalmoney());
            dto.setServiceFeeTotalmoney(m.getServiceFeeTotalmoney());
            dto.setSettlementCode(m.getSettlementCode());
            dto.setReceiveTotalmoney(m.getReceiveTotalmoney());
            dto.setSettlementStaffName(m.getSettlementStaffName());
            dto.setSettleStartDate(DateFormatUtil.dateToString(m.getSettleStartDate(), CommonConstant.JSON_FORMAT_PATTERN_DATE)
              + "-" + DateFormatUtil.dateToString(m.getSettleEndDate(), CommonConstant.JSON_FORMAT_PATTERN_DATE));
            result.add(dto);
        });
    return result;
    }
}

package cn.net.yzl.crm.service.settlement.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.settlement.SettlementExpressService;
import cn.net.yzl.logistics.model.ExpressSettlementExcelDTO;
import cn.net.yzl.logistics.model.vo.ExpressSettlementPageVo;
import cn.net.yzl.logistics.settleexpresscharge.SettleBillSearchVo;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SettlementExpressServiceImpl implements SettlementExpressService {

    @Autowired
    private LogisticsFien logisticsFien;


    @Override
    public void exportSettleExcel(SettleBillSearchVo searchVo, HttpServletResponse response) throws IOException {
        ComResponse<Page<ExpressSettlementPageVo>> pageComResponse = logisticsFien.searchSettBill(searchVo);
        Page<ExpressSettlementPageVo> data = pageComResponse.getData();
        if(data != null) {
            List<ExpressSettlementPageVo> items = data.getItems();
            if (!CollectionUtils.isEmpty(items)) {
                int i =0 ;
                for (ExpressSettlementPageVo item : items) {
                    ExpressSettlementExcelDTO copy = CglibUtil.copy(item, ExpressSettlementExcelDTO.class);
                    String startDate = DateUtil.format(item.getGenerSettleDateStart(), "yyyy-MM-dd");
                    String endDate = DateUtil.format(item.getGenerSettleDateEnd(), "yyyy-MM-dd");
                    copy.setSettleDate(startDate+"到"+endDate);
                    copy.setIndex(i);
                    i++;
                }
                List<ExpressSettlementExcelDTO> excelDTOS = CglibUtil.copyList(items, ExpressSettlementExcelDTO::new);
                response.setCharacterEncoding("UTF-8");
                //响应内容格式

                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sysDate = simpleDateFormat.format(date);
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;fileName=" +"spcgyj"+sysDate +".xlsx");

                //向前端写入文件流流
                EasyExcel.write(response.getOutputStream(), ProductPurchaseWarnExcelDTO.class)
                        .sheet("商品库存预警").doWrite(excelDTOS);
                return;
            }
        }
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(pageComResponse));
    }
}

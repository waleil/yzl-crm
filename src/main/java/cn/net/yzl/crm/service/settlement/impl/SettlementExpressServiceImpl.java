package cn.net.yzl.crm.service.settlement.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.settlement.SettlementExpressService;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.logistics.model.ExpressSettlementExcelDTO;
import cn.net.yzl.logistics.model.vo.ExpressSettlementPageVo;
import cn.net.yzl.logistics.settleexpresscharge.ExpressChargeSettlementDetail;
import cn.net.yzl.logistics.settleexpresscharge.SettleBillSearchVo;
import cn.net.yzl.logistics.settleexpresscharge.SettlementDetailResult;
import cn.net.yzl.logistics.settleexpresscharge.excel.SettlementDetailExcel;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SettlementExpressServiceImpl implements SettlementExpressService {

    @Autowired
    private LogisticsFien logisticsFien;


//    @Override
//    public void exportSettleExcel(SettleBillSearchVo searchVo, HttpServletResponse response) throws IOException {
//        ComResponse<Page<ExpressSettlementPageVo>> pageComResponse = logisticsFien.searchSettBill(searchVo);
//        Page<ExpressSettlementPageVo> data = pageComResponse.getData();
//        if(data != null) {
//            List<ExpressSettlementPageVo> items = data.getItems();
//            if (!CollectionUtils.isEmpty(items)) {
//                int i =1;
//                List<ExpressSettlementExcelDTO> excelDTOS = new ArrayList<>();
//                for (ExpressSettlementPageVo item : items) {
//                    ExpressSettlementExcelDTO copy = CglibUtil.copy(item, ExpressSettlementExcelDTO.class);
//                    String startDate = DateUtil.format(item.getGenerSettleDateStart(), "yyyy-MM-dd");
//                    String endDate = DateUtil.format(item.getGenerSettleDateEnd(), "yyyy-MM-dd");
//                    copy.setSettleDate(startDate+"???"+endDate);
//                    copy.setIndex(i);
//                    i++;
//                    excelDTOS.add(copy);
//                }
//                response.setCharacterEncoding("UTF-8");
//                //??????????????????
//
//                String sysDate =DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
//                response.setContentType("application/vnd.ms-excel");
//                response.setHeader("Content-Disposition", "attachment;fileName="+"JSYF"+sysDate +".xlsx");
//
//                //???????????????????????????
//                EasyExcel.write(response.getOutputStream(), ExpressSettlementExcelDTO.class)
//                        .sheet("??????????????????").doWrite(excelDTOS);
//                return;
//            }
//        }
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        out.write(JSON.toJSONString(pageComResponse));
//
//
//
//    }


    @Override
    public void exportSettleExcel(SettleBillSearchVo searchVo, HttpServletResponse response) throws IOException {
        ComResponse<List<ExpressSettlementPageVo>> comResponse = logisticsFien.searchSettBillList(searchVo);
        log.info("????????????????????????????????????:{}", JsonUtil.toJsonStr(comResponse));
        if (comResponse==null || comResponse.getCode() !=200L){
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(comResponse));
            return;
        }
        List<ExpressSettlementPageVo> listComResponseData = comResponse.getData();
        if (CollectionUtils.isEmpty(listComResponseData)){
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(listComResponseData));
            return;
        }
        int i =1 ;
        List<ExpressSettlementExcelDTO> excels = new ArrayList<>();
        for (ExpressSettlementPageVo item : listComResponseData) {
            ExpressSettlementExcelDTO copy = CglibUtil.copy(item, ExpressSettlementExcelDTO.class);
            String startDate = DateUtil.format(item.getGenerSettleDateStart(), "yyyy-MM-dd");
            String endDate = DateUtil.format(item.getGenerSettleDateEnd(), "yyyy-MM-dd");
            copy.setSettleDate(startDate+"???"+endDate);
            copy.setIndex(i);
            i++;
            excels.add(copy);
        }
        log.info("??????????????????excel??????:{}", JsonUtil.toJsonStr(excels));
        //????????????
        String sysDate =DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        response.setCharacterEncoding("UTF-8");
        //??????????????????

        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment;fileName=" +"JSYF"+sysDate +".xlsx");

        response.setHeader("Content-Disposition", "attachment;fileName="+
                URLEncoder.encode("??????????????????"+sysDate+".xlsx", "utf-8"));
        //???????????????????????????
        EasyExcel.write(response.getOutputStream(), ExpressSettlementExcelDTO.class)
                .registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
                .sheet("??????????????????").doWrite(excels);

    }

//    @Override
//    public void exportSettleOverDetailExcel( ExpressChargeSettlementDetail detail, HttpServletResponse response) throws IOException {
//        ComResponse<Page<SettlementDetailResult>> pageComResponse = logisticsFien.expressChargeSettlementDetailSearch(detail);
//        Page<SettlementDetailResult> data = pageComResponse.getData();
//        if(data != null) {
//            List<SettlementDetailResult> items = data.getItems();
//            if (!CollectionUtils.isEmpty(items)) {
//                int i =1 ;
//                List<SettlementDetailExcel> excels = new ArrayList<>();
//                for (SettlementDetailResult item : items) {
//                    SettlementDetailExcel copy = CglibUtil.copy(item, SettlementDetailExcel.class);
//                    copy.setSignTime(DateUtil.format(item.getSignTime(), "yyyy-MM-dd"));
//                    copy.setOctime(DateUtil.format(item.getOctime(), "yyyy-MM-dd"));
//                    copy.setIndex(i);
//                    i++;
//                    excels.add(copy);
//                }
//                response.setCharacterEncoding("UTF-8");
//                //??????????????????
//
//                String sysDate =DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
//                response.setContentType("application/vnd.ms-excel");
//                response.setHeader("Content-Disposition", "attachment;fileName=" +"yfjsmx"+sysDate +".xlsx");
//
//                //???????????????????????????
//                EasyExcel.write(response.getOutputStream(), SettlementDetailExcel.class)
//                        .sheet("??????????????????").doWrite(excels);
//                return;
//            }
//        }
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        out.write(JSON.toJSONString(pageComResponse));
//    }

    @Override
    public void exportSettleOverDetailExcel( ExpressChargeSettlementDetail detail, HttpServletResponse response) throws IOException {
        ComResponse<List<SettlementDetailResult>> pageComResponse = logisticsFien.expressChargeSettlementDetailList(detail);

        log.info("??????????????????????????????:{}", JsonUtil.toJsonStr(pageComResponse));
        if (pageComResponse==null || pageComResponse.getCode() !=200L){
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(pageComResponse));
            return;
        }
        List<SettlementDetailResult> listComResponseData = pageComResponse.getData();
        if (CollectionUtils.isEmpty(listComResponseData)){
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(listComResponseData));
            return;
        }


        int i =1 ;
        List<SettlementDetailExcel> excels = new ArrayList<>();
        for (SettlementDetailResult item : listComResponseData) {
            SettlementDetailExcel copy = CglibUtil.copy(item, SettlementDetailExcel.class);
            copy.setSignTime(DateUtil.format(item.getSignTime(), "yyyy-MM-dd"));
            copy.setOctime(DateUtil.format(item.getOctime(), "yyyy-MM-dd"));
            copy.setIndex(i);
            i++;
            excels.add(copy);
        }

        log.info("??????????????????excel??????:{}", JsonUtil.toJsonStr(excels));
        //????????????
        String sysDate =DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        response.setCharacterEncoding("UTF-8");
        //??????????????????

        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment;fileName=YFJSMX"+sysDate+".xlsx");


        response.setHeader("Content-Disposition", "attachment;fileName="+
                URLEncoder.encode("??????????????????"+sysDate+".xlsx", "utf-8"));
        //???????????????????????????
        EasyExcel.write(response.getOutputStream(), SettlementDetailExcel.class)
                .registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
                .sheet("??????????????????").doWrite(excels);
    }
}

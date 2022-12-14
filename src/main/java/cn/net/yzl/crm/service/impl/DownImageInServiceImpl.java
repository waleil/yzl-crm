package cn.net.yzl.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.client.store.ProductPurchaseWarnFeignService;
import cn.net.yzl.crm.client.store.ProductStockFeignService;
import cn.net.yzl.crm.client.store.RemoveStockFeignService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.logistics.print.AppreciationDTOS;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.dto.express.*;
import cn.net.yzl.model.vo.OutStoreOrderInfoParamVo;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 12:47
 */
@Service
@Slf4j
public class DownImageInServiceImpl implements DownImageInService {

    @Autowired
    private ProductStockFeignService productStockFeignService;

    @Autowired
    private ProductPurchaseWarnFeignService feignService;

    @Autowired
    private RemoveStockFeignService removeStockFeignService;

    @Override
    public void exportProductStockExcel(String codeAndName, String storeNo, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ProductStockExcelVo>> listComResponse = productStockFeignService.selectProductStockExcel(codeAndName, storeNo);

        if (listComResponse==null || listComResponse.getCode() !=200){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(listComResponse));
            return;
        }
        List<ProductStockExcelVo> listComResponseData = listComResponse.getData();
        if (listComResponseData == null || listComResponseData.size()==0){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(listComResponse));
            return;
        }

        //????????????
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysDate = simpleDateFormat.format(date);

        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +"productStock"+sysDate +".xlsx");


        //????????????
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = ExcelStyleUtils.getHorizontalCellStyleStrategy();

        //???????????????????????????
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductStockExcelVo.class).registerWriteHandler(horizontalCellStyleStrategy)
                .sheet("?????????").doWrite(listComResponseData);
    }


    /**
     * ????????????????????????
     * @param productPurchaseWarnExcelVO
     * @param httpServletResponse
     */
    @Override
    public void exportExcelOfProductPurchaseWarn(ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ProductPurchaseWarnExcelDTO>> listComResponse = feignService.selectExcelOfProductPurchaseWarn(productPurchaseWarnExcelVO);
        if (listComResponse==null || listComResponse.getCode() !=200L){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(listComResponse));
            return;
        }
        List<ProductPurchaseWarnExcelDTO> listComResponseData = listComResponse.getData();
        if (listComResponseData == null || listComResponseData.size()==0){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(listComResponse));
            return;
        }
        //????????????
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysDate = simpleDateFormat.format(date);

        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +"spcgyj"+sysDate +".xlsx");

        //????????????
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = ExcelStyleUtils.getHorizontalCellStyleStrategy();

        //???????????????????????????
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductPurchaseWarnExcelDTO.class).registerWriteHandler(horizontalCellStyleStrategy)
                .sheet("??????????????????").doWrite(listComResponseData);
    }

    @Override
    public ComResponse<Map<String,Long>> getExpressExportCount(OutStoreOrderInfoParamVo outStoreOrderInfoParamVo){
        String expressNo = outStoreOrderInfoParamVo.getExpressNo();
        if(!("DEPPON".equals(expressNo) || "YUNDA".equals(expressNo) || "EMS".equals(expressNo)
                || "sjzyj".equals(expressNo) || "bdyj".equals(expressNo))){
            return ComResponse.fail(ResponseCodeEnums.ERROR.getCode(),"????????????????????????????????????????????????");
        }
        long l = System.currentTimeMillis();
//        outStoreOrderInfoParamVo.setTime(l);
        ComResponse<List<ExpressOrderInfo>> senderExpressInfo = removeStockFeignService.getSenderExpressInfo(outStoreOrderInfoParamVo);
        List<ExpressOrderInfo> data = senderExpressInfo.getData();
        int i =0;
        if(!CollectionUtils.isEmpty(data)){
            if("YUNDA".equals(expressNo)){
                for (ExpressOrderInfo datum : data) {
                    if (datum.getIfProxyCollect()) {
                        i++;
                    }
                }
                if(i==0){
                    return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE.getCode(),"?????????????????????????????????????????????");
                }
            }else{
                i=data.size();
            }
            Map<String,Long> map = new HashMap<>();
            map.put("time",l);
            return ComResponse.success(200,1,"??????????????????????????????????????????"+i+"???!",map);
        }else{
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE.getCode(),"?????????????????????????????????????????????");
        }

    }

    @Override
    public void exportExpressPrintInfo(OutStoreOrderInfoParamVo outStoreOrderInfoParamVo, HttpServletResponse httpServletResponse) throws IOException {
        String expressNo = outStoreOrderInfoParamVo.getExpressNo();
        ComResponse<List<ExpressOrderInfo>> senderExpressInfo = removeStockFeignService.getSenderExpressInfo(outStoreOrderInfoParamVo);
        log.info("????????????????????????:{}", JsonUtil.toJsonStr(senderExpressInfo));
        if (senderExpressInfo==null || senderExpressInfo.getCode() !=200L){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(senderExpressInfo));
            return;
        }
        List<ExpressOrderInfo> expressOrderInfos = senderExpressInfo.getData();
        //TODO
        if("DEPPON".equals(expressNo)){
            handleDP(expressOrderInfos,httpServletResponse);
        }else if ("YUNDA".equals(expressNo)){
            handleYUNDA(expressOrderInfos,httpServletResponse);
        }else if ("EMS".equals(expressNo) || "sjzyj".equals(expressNo) || "bdyj".equals(expressNo)){
            handleEMS(expressOrderInfos,httpServletResponse);
        }
    }

    private void handleEMS(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<PostalExcelModel> postalExcelModels = new ArrayList<>();
        List<PostalExcelModel> ZBExcelModels = new ArrayList<>();
        List<PostalExcelModel> XTExcelModels = new ArrayList<>();
        List<PostalExcelModel> QXExcelModels = new ArrayList<>();
        List<PostalExcelModel> FBExcelModels = new ArrayList<>();

        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=EMS_"+sysDate+".xlsx");
        if (CollectionUtils.isEmpty(expressOrderInfos)){
            EasyExcel.write(httpServletResponse.getOutputStream(), PostalExcelModel.class)
                    .sheet("????????????").doWrite(postalExcelModels);
            return;
        }
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            PostalExcelModel postalExcelModel = new PostalExcelModel();
            postalExcelModel.setExpressNum(expressOrderInfo.getDeliverCode());
            postalExcelModel.setOprName(expressOrderInfo.getOprName());
            postalExcelModel.setPhone(expressOrderInfo.getPhone());
            String sendAddrDetail = StrUtil.builder(expressOrderInfo.getProvince()).append(expressOrderInfo.getCity())
                    .append(expressOrderInfo.getArea()).append(expressOrderInfo.getAddr()).toString();
            postalExcelModel.setSendAddr(sendAddrDetail);
            postalExcelModel.setMemberName(expressOrderInfo.getMemberName());
            String memberPhone = expressOrderInfo.getMemberPhone();
            if(StrUtil.contains(memberPhone,",")){
                String[] split = StrUtil.split(memberPhone, ",");
                postalExcelModel.setMemberPhone(split[0]);
                postalExcelModel.setMemberPhone2(split[1]);
            }else{
                postalExcelModel.setMemberPhone(memberPhone);
            }
            String targetAddrDetail = StrUtil.builder(expressOrderInfo.getTargetProvince()).append(expressOrderInfo.getTargetCity())
                    .append(expressOrderInfo.getTargetArea()).append(expressOrderInfo.getTargetAddr()).toString();
            postalExcelModel.setTargetAddr(targetAddrDetail);
            postalExcelModel.setWight("0.5");
            postalExcelModel.setRemark(expressOrderInfo.getProductName());

            String payType = expressOrderInfo.getPayType();

//            if(expressOrderInfo.getIfProxyCollect()) {

                postalExcelModel.setAgentMoney(expressOrderInfo.getIfProxyCollect()?"???":"???");
//                postalExcelModel.setReceivableMoney(expressOrderInfo.getCash());
                postalExcelModel.setReceivableMoney(handleMoney(expressOrderInfo.getCash()));
//            }else{
//                postalExcelModel.setAgentMoney("???");
//                postalExcelModel.setReceivableMoney("0");
//            }
            postalExcelModel.setInInfo(expressOrderInfo.getDeliverCode());
            String deliverCode = expressOrderInfo.getDeliverCode();
            if(StrUtil.startWith(deliverCode,"ZB")){
                ZBExcelModels.add(postalExcelModel);
            }else if (StrUtil.startWith(deliverCode,"XT")){
                XTExcelModels.add(postalExcelModel);
            }else if (StrUtil.startWith(deliverCode,"QX")){
                QXExcelModels.add(postalExcelModel);

            }else if (StrUtil.startWith(deliverCode,"FB")){
                FBExcelModels.add(postalExcelModel);
            }else{
                postalExcelModels.add(postalExcelModel);
            }
        }
        List<ByteArrayOutputStream> outputStreams = new ArrayList<>();
        ByteArrayOutputStream zb = createEMSExcel(ZBExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream XT = createEMSExcel(XTExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream QX = createEMSExcel(QXExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream fb = createEMSExcel(FBExcelModels, "??????????????????????????????????????????");
        ByteArrayOutputStream other = createEMSExcel(postalExcelModels, "??????");
        outputStreams.add(zb);
        outputStreams.add(XT);
        outputStreams.add(QX);
        outputStreams.add(fb);
        outputStreams.add(other);
        List<String> fileNames= new ArrayList<>();
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("??????????????????????????????????????????.xlsx");
        fileNames.add("??????.xlsx");
        createZip(httpServletResponse,outputStreams,fileNames,"EMS");
    }

    private void handleDP(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<NewDPExcelModel> dpExcelModels = new ArrayList<>();
        List<NewDPExcelModel> ZBExcelModels = new ArrayList<>();
        List<NewDPExcelModel> XTExcelModels = new ArrayList<>();
        List<NewDPExcelModel> QXExcelModels = new ArrayList<>();
        List<NewDPExcelModel> FBExcelModels = new ArrayList<>();
        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="
                +URLEncoder.encode("DEPPON_"+sysDate+".xlsx", "utf-8"));
        if(CollectionUtils.isEmpty(expressOrderInfos)){
            EasyExcel.write(httpServletResponse.getOutputStream(), NewDPExcelModel.class)
                    .sheet("????????????").doWrite(dpExcelModels);
            return;
        }
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            NewDPExcelModel dpExcelModel = new NewDPExcelModel();
            dpExcelModel.setOrderNo(expressOrderInfo.getDeliverCode());
            dpExcelModel.setOprName(expressOrderInfo.getOprName());
            dpExcelModel.setPhone(expressOrderInfo.getPhone());
            String sendAddrDetail = StrUtil.builder(expressOrderInfo.getProvince()).append(expressOrderInfo.getCity())
                    .append(expressOrderInfo.getArea()).append(expressOrderInfo.getAddr()).toString();
            dpExcelModel.setSendAddr(sendAddrDetail);
            dpExcelModel.setMemberName(expressOrderInfo.getMemberName());
            dpExcelModel.setMobilePhone(expressOrderInfo.getMemberPhone());
            String targetAddrDetail = StrUtil.builder(expressOrderInfo.getTargetProvince()).append(expressOrderInfo.getTargetCity())
                    .append(expressOrderInfo.getTargetArea()).append(expressOrderInfo.getTargetAddr()).toString();
            dpExcelModel.setAddr(targetAddrDetail);
            dpExcelModel.setNature("???????????????");
            dpExcelModel.setFreightType("??????");
            dpExcelModel.setProductName(expressOrderInfo.getProductName());
            dpExcelModel.setProxyReturnMoney("?????????");
            dpExcelModel.setProxyMoney(handleMoney(expressOrderInfo.getCash()));
            dpExcelModel.setProxyAccount(expressOrderInfo.getMonthAccount());
            dpExcelModel.setOpenName(expressOrderInfo.getFinancialOwner());
            String deliverCode = expressOrderInfo.getDeliverCode();
            if(StrUtil.startWith(deliverCode,"ZB")){
                ZBExcelModels.add(dpExcelModel);
            }else if (StrUtil.startWith(deliverCode,"XT")){
                XTExcelModels.add(dpExcelModel);
            }else if (StrUtil.startWith(deliverCode,"QX")){
                QXExcelModels.add(dpExcelModel);
            }else if (StrUtil.startWith(deliverCode,"FB")){
                FBExcelModels.add(dpExcelModel);
            }else{
                dpExcelModels.add(dpExcelModel);
            }
        }

        List<ByteArrayOutputStream> outputStreams = new ArrayList<>();
        ByteArrayOutputStream zb = createDPExcel(ZBExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream XT = createDPExcel(XTExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream QX = createDPExcel(QXExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream fb = createDPExcel(FBExcelModels, "??????????????????????????????????????????");
        ByteArrayOutputStream other = createDPExcel(dpExcelModels, "??????");
        outputStreams.add(zb);
        outputStreams.add(XT);
        outputStreams.add(QX);
        outputStreams.add(fb);
        outputStreams.add(other);
        List<String> fileNames= new ArrayList<>();
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("??????????????????????????????????????????.xlsx");
        fileNames.add("??????.xlsx");
        createZip(httpServletResponse,outputStreams,fileNames,"??????");
//        log.info("??????????????????:{}",JsonUtil.toJsonStr(dpExcelModels));

        //???????????????????????????
//        EasyExcel.write(httpServletResponse.getOutputStream(), NewDPExcelModel.class)
//                .sheet("????????????").doWrite(dpExcelModels);

    }

    private void handleYUNDA(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<YunDaExcelModel> yunDaExcelModels = new ArrayList<>();
        List<YunDaExcelModel> ZBExcelModels = new ArrayList<>();
        List<YunDaExcelModel> XTExcelModels = new ArrayList<>();
        List<YunDaExcelModel> QXExcelModels = new ArrayList<>();
        List<YunDaExcelModel> FBExcelModels = new ArrayList<>();
        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="
                +URLEncoder.encode("YUNDA_"+sysDate+".xlsx", "utf-8"));
        if (CollectionUtils.isEmpty(expressOrderInfos)){
            EasyExcel.write(httpServletResponse.getOutputStream(), YunDaExcelModel.class)
                    .sheet("????????????").doWrite(yunDaExcelModels);
            return;
        }
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            if(!expressOrderInfo.getIfProxyCollect()){
                continue;
            }
            YunDaExcelModel yunDaExcelModel = new YunDaExcelModel();
            yunDaExcelModel.setExpressNum(expressOrderInfo.getDeliverCode());
            yunDaExcelModel.setOprName(expressOrderInfo.getOprName());
            yunDaExcelModel.setPhone(expressOrderInfo.getPhone());
            String sendAddrDetail = StrUtil.builder(expressOrderInfo.getProvince()).append(expressOrderInfo.getCity())
                    .append(expressOrderInfo.getArea()).append(expressOrderInfo.getAddr()).toString();
            yunDaExcelModel.setSendAddr(sendAddrDetail);
            yunDaExcelModel.setMemberName(expressOrderInfo.getMemberName());
            yunDaExcelModel.setMobilePhone(expressOrderInfo.getMemberPhone());
            String targetAddrDetail = StrUtil.builder(expressOrderInfo.getTargetProvince()).append(expressOrderInfo.getTargetCity())
                    .append(expressOrderInfo.getTargetArea()).append(expressOrderInfo.getTargetAddr()).toString();
            yunDaExcelModel.setAddrDetail(targetAddrDetail);
            yunDaExcelModel.setContent(expressOrderInfo.getProductName());
            yunDaExcelModel.setMoney(handleMoney(expressOrderInfo.getCash()));
            yunDaExcelModel.setCustom1(expressOrderInfo.getDeliverCode());
            yunDaExcelModels.add(yunDaExcelModel);
            String deliverCode = expressOrderInfo.getDeliverCode();
            if(StrUtil.startWith(deliverCode,"ZB")){
                ZBExcelModels.add(yunDaExcelModel);
            }else if (StrUtil.startWith(deliverCode,"XT")){
                XTExcelModels.add(yunDaExcelModel);
            }else if (StrUtil.startWith(deliverCode,"QX")){
                QXExcelModels.add(yunDaExcelModel);
            }else if (StrUtil.startWith(deliverCode,"FB")){
                FBExcelModels.add(yunDaExcelModel);
            }else{
                yunDaExcelModels.add(yunDaExcelModel);
            }
        }
        log.info("??????????????????:{}",JsonUtil.toJsonStr(yunDaExcelModels));
        List<ByteArrayOutputStream> outputStreams = new ArrayList<>();
        ByteArrayOutputStream zb = createYunDaExcel(ZBExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream XT = createYunDaExcel(XTExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream QX = createYunDaExcel(QXExcelModels, "???????????????????????????????????????");
        ByteArrayOutputStream fb = createYunDaExcel(FBExcelModels, "??????????????????????????????????????????");
        ByteArrayOutputStream other = createYunDaExcel(yunDaExcelModels, "??????");
        outputStreams.add(zb);
        outputStreams.add(XT);
        outputStreams.add(QX);
        outputStreams.add(fb);
        outputStreams.add(other);
        List<String> fileNames= new ArrayList<>();
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("???????????????????????????????????????.xlsx");
        fileNames.add("??????????????????????????????????????????.xlsx");
        fileNames.add("??????.xlsx");
        createZip(httpServletResponse,outputStreams,fileNames,"YUNDA");
    }

    private String handleMoney(String money){
        if(StrUtil.isBlank(money)){
            return "0";
        }
        return NumberUtil.toStr(NumberUtil.div(money, "100"));
    }


    public void createZip(HttpServletResponse response,List<ByteArrayOutputStream> bosList,List<String> fileNames,String zipName) {
        ZipOutputStream zos= null;
        try {
            //??????HttpServerResponse????????????
            OutputStream out=response.getOutputStream();
            //???????????????
            BufferedInputStream bis;
            //????????????????????????
            File file=new File(zipName+".zip");
            //??????ZipOutputStream????????????????????????
            zos=new ZipOutputStream(new FileOutputStream(file));
            //??????????????????excle????????????????????????
            writeZos(bosList, zos,fileNames);
            //??????????????????
            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(zipName+".zip", "UTF-8"))));
            bis = new BufferedInputStream(new FileInputStream(zipName+".zip"));
            //??????byte????????????????????????zip?????????byte???????????????????????????
            byte[] buffer=new byte[bis.available()];
            bis.read(buffer);
            out.flush();
            out.write(buffer);
        }catch (IOException e){
            e.printStackTrace();
            log.info("??????????????????:{}",e);
        }finally {
            try {
                if(zos != null){
                    zos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
                log.info("??????????????????:{}",e);
            }

        }

    }

    public void writeZos(List<ByteArrayOutputStream> bosList, ZipOutputStream zos,List<String> fileNames) throws IOException{
        for (int i = 0; i < bosList.size(); i++) {
            ByteArrayOutputStream outputStream = bosList.get(i);
            if(outputStream == null){
                continue;
            }
            //?????????excel????????????????????????
            zos.putNextEntry(new ZipEntry(fileNames.get(i)));
            byte[] excelStream= outputStream.toByteArray();
            zos.write(excelStream);
            //????????????
            zos.closeEntry();
        }
    }

    public ByteArrayOutputStream createYunDaExcel(List<YunDaExcelModel> yunDaExcelModels,String fileName) throws UnsupportedEncodingException {
        if(CollectionUtils.isEmpty(yunDaExcelModels)){
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EasyExcel.write(outputStream, YunDaExcelModel.class)
                .registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
                .sheet("????????????").doWrite(yunDaExcelModels);
        URLEncoder.encode(fileName, "utf-8");
        return outputStream;
    }

    public ByteArrayOutputStream createEMSExcel(List<PostalExcelModel> postalExcelModels,String fileName) throws UnsupportedEncodingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(CollectionUtils.isEmpty(postalExcelModels)){
            return null;
        }
        EasyExcel.write(outputStream, PostalExcelModel.class)
                .registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
                .sheet("????????????").doWrite(postalExcelModels);
        URLEncoder.encode(fileName, "utf-8");
        return outputStream;
    }

    public ByteArrayOutputStream createDPExcel(List<NewDPExcelModel> newDPExcelModels,String fileName) throws UnsupportedEncodingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(CollectionUtils.isEmpty(newDPExcelModels)){
            return null;
        }
        EasyExcel.write(outputStream, NewDPExcelModel.class)
                .registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
                .sheet("????????????").doWrite(newDPExcelModels);
        URLEncoder.encode(fileName, "utf-8");
        return outputStream;
    }
}

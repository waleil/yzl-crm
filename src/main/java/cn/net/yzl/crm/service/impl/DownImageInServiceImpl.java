package cn.net.yzl.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.client.store.ProductPurchaseWarnFeignService;
import cn.net.yzl.crm.client.store.ProductStockFeignService;
import cn.net.yzl.crm.client.store.RemoveStockFeignService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.logistics.print.AppreciationDTOS;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.dto.express.*;
import cn.net.yzl.model.vo.OutStoreOrderInfoParamVo;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        //系统时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysDate = simpleDateFormat.format(date);

        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +"productStock"+sysDate +".xlsx");



        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductStockExcelVo.class)
                .sheet("库存表").doWrite(listComResponseData);
    }


    /**
     * 导出商品采购预警
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
        //系统时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysDate = simpleDateFormat.format(date);

        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +"spcgyj"+sysDate +".xlsx");

        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductPurchaseWarnExcelDTO.class)
                .sheet("商品库存预警").doWrite(listComResponseData);
    }

    public void exportExpressPrintInfo(OutStoreOrderInfoParamVo outStoreOrderInfoParamVo, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ExpressOrderInfo>> senderExpressInfo = removeStockFeignService.getSenderExpressInfo(outStoreOrderInfoParamVo);
        log.info("仓储中心数据物流:{}", JsonUtil.toJsonStr(senderExpressInfo));
        if (senderExpressInfo==null || senderExpressInfo.getCode() !=200L){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(senderExpressInfo));
            return;
        }
        List<ExpressOrderInfo> expressOrderInfos = senderExpressInfo.getData();
        if (CollectionUtils.isEmpty(expressOrderInfos)){
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSON.toJSONString(expressOrderInfos));
            return;
        }
        ExpressOrderInfo expressOrderInfo = expressOrderInfos.get(0);
        String expressNo = expressOrderInfo.getExpressNo();
        //TODO
        if("DEPPON".equals(expressNo)){
            handleDP(expressOrderInfos,httpServletResponse);
        }else if ("YUNDA".equals(expressNo)){
            handleYUNDA(expressOrderInfos,httpServletResponse);
        }else if ("EMS".equals(expressNo)){
            handleEMS(expressOrderInfos,httpServletResponse);
        }
    }

    private void handleEMS(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<PostalExcelModel> postalExcelModels = new ArrayList<>();
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            PostalExcelModel postalExcelModel = new PostalExcelModel();
            postalExcelModel.setExpressNum(expressOrderInfo.getExpressNum());
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
            if("1".equals(payType)) {

                postalExcelModel.setAgentMoney("是");
//                postalExcelModel.setReceivableMoney(expressOrderInfo.getCash());
                postalExcelModel.setReceivableMoney(handleMoney(expressOrderInfo.getCash()));
            }else{
                postalExcelModel.setAgentMoney("否");
                postalExcelModel.setReceivableMoney("0");
            }
            postalExcelModel.setInInfo(expressOrderInfo.getDeliverCode());
            postalExcelModels.add(postalExcelModel);
        }

        //系统时间
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sysDate = simpleDateFormat.format(date);
        log.info("EMS表格数据:{}",JsonUtil.toJsonStr(postalExcelModels));
        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=EMS_"+sysDate+".xlsx");

        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), PostalExcelModel.class)
                .sheet("导入模板").doWrite(postalExcelModels);
    }

    private void handleDP(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<NewDPExcelModel> dpExcelModels = new ArrayList<>();
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            NewDPExcelModel dpExcelModel = new NewDPExcelModel();
            dpExcelModel.setOrderNo(expressOrderInfo.getDeliverCode());
            dpExcelModel.setOprName(expressOrderInfo.getOprName());
            dpExcelModel.setPhone(expressOrderInfo.getPhone());
            String sendAddrDetail = StrUtil.builder(expressOrderInfo.getProvince()).append(expressOrderInfo.getCity())
                    .append(expressOrderInfo.getArea()).append(expressOrderInfo.getAddr()).toString();
            dpExcelModel.setSendAddr(sendAddrDetail);
            dpExcelModel.setMemberName(expressOrderInfo.getMemberName());
            dpExcelModel.setMemberPhone(expressOrderInfo.getMemberPhone());
            String targetAddrDetail = StrUtil.builder(expressOrderInfo.getTargetProvince()).append(expressOrderInfo.getTargetCity())
                    .append(expressOrderInfo.getTargetArea()).append(expressOrderInfo.getTargetAddr()).toString();
            dpExcelModel.setAddr(targetAddrDetail);
            dpExcelModel.setNature("微小件特惠");
            dpExcelModel.setFreightType("月结");
            dpExcelModel.setProductName(expressOrderInfo.getProductName());
            dpExcelModel.setProxyReturnMoney("三日退");
            dpExcelModel.setProxyMoney(handleMoney(expressOrderInfo.getCash()));
//            dpExcelModel.setOpenName();
            dpExcelModel.setProxyAccount(expressOrderInfo.getMonthAccount());
            dpExcelModels.add(dpExcelModel);
        }

        log.info("德邦表格数据:{}",JsonUtil.toJsonStr(dpExcelModels));
        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="
                +URLEncoder.encode("德邦_"+sysDate+".xlsx", "utf-8")+".xlsx");

        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), NewDPExcelModel.class)
                .sheet("导入模板").doWrite(dpExcelModels);

    }

    private void handleYUNDA(List<ExpressOrderInfo> expressOrderInfos, HttpServletResponse httpServletResponse) throws IOException{
        List<YunDaExcelModel> yunDaExcelModels = new ArrayList<>();
        for (ExpressOrderInfo expressOrderInfo : expressOrderInfos) {
            YunDaExcelModel yunDaExcelModel = new YunDaExcelModel();
            yunDaExcelModel.setExpressNum(expressOrderInfo.getDeliverCode());
            yunDaExcelModel.setOprName(expressOrderInfo.getOprName());
            yunDaExcelModel.setPhone(expressOrderInfo.getPhone());
            String sendAddrDetail = StrUtil.builder(expressOrderInfo.getProvince()).append(expressOrderInfo.getCity())
                    .append(expressOrderInfo.getArea()).append(expressOrderInfo.getAddr()).toString();
            yunDaExcelModel.setSendAddr(sendAddrDetail);
            yunDaExcelModel.setMemberName(expressOrderInfo.getMemberName());
            yunDaExcelModel.setMemberPhone(expressOrderInfo.getMemberPhone());
            String targetAddrDetail = StrUtil.builder(expressOrderInfo.getTargetProvince()).append(expressOrderInfo.getTargetCity())
                    .append(expressOrderInfo.getTargetArea()).append(expressOrderInfo.getTargetAddr()).toString();
            yunDaExcelModel.setAddrDetail(targetAddrDetail);
            yunDaExcelModel.setContent(expressOrderInfo.getProductName());
            yunDaExcelModel.setMoney(expressOrderInfo.getCash());
            yunDaExcelModel.setCustom1(expressOrderInfo.getDeliverCode());
            yunDaExcelModels.add(yunDaExcelModel);
        }
        log.info("韵达表格数据:{}",JsonUtil.toJsonStr(yunDaExcelModels));
        String sysDate = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="
                +URLEncoder.encode("韵达_"+sysDate+".xlsx", "utf-8")+".xlsx");

        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), YunDaExcelModel.class)
                .sheet("导入模板").doWrite(yunDaExcelModels);
    }

    private String handleMoney(String money){
        if(StrUtil.isBlank(money)){
            return "0";
        }
        return NumberUtil.toStr(NumberUtil.div(money, "100"));
    }
}

package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.store.ProductPurchaseWarnFeignService;
import cn.net.yzl.crm.client.store.ProductStockFeignService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 12:47
 */
@Service
public class DownImageInServiceImpl implements DownImageInService {

    @Autowired
    private ProductStockFeignService productStockFeignService;

    @Autowired
    private ProductPurchaseWarnFeignService feignService;


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
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" + new String("库存信息".getBytes(),"iso-8859-1")+sysDate +".xlsx");



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
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +new String("商品采购预警信息".getBytes(),"iso-8859-1")+sysDate +".xlsx");

        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductPurchaseWarnExcelDTO.class)
                .sheet("商品库存预警").doWrite(listComResponseData);
    }
}

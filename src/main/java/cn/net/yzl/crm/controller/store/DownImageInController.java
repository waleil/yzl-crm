package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.store.InventoryFeginService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.model.vo.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/20 10:07
 */
@Controller
@Api(value = "仓储中心心心心心-下载", tags = {"仓储中心心心心心-下载"})
@RequestMapping("down")
public class DownImageInController {

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private InventoryFeginService inventoryFeginService;

    @Autowired
    private DownImageInService downImageInService;

//    @Value("${supplierDown.url}")
//    private String baseUrl;

    @ApiOperation("下载图片")
    @GetMapping("v1/downImage")
    public void downImage(@RequestParam("imageUrl") String imageUrl, HttpServletResponse httpServletResponse) throws IOException {
        if (StringUtils.isEmpty(imageUrl)){

        }
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {

//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String date = simpleDateFormat.format(new Date());

            inputStream = fastdfsUtils.download(imageUrl, null);
            String[] split = imageUrl.split("[.]");
            String[] splitPath = split[0].split("/");
            httpServletResponse.setContentType("image/" + split[split.length - 1]);
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+splitPath[splitPath.length-1]+"."+split[split.length-1]);
            outputStream = httpServletResponse.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } finally {
            if (outputStream != null)
                outputStream.close();
            if (inputStream != null)
                inputStream.close();
        }
    }

    @ApiOperation(value = "导出查看盘点商品(附带下载路径)",notes = "导出查看盘点商品(附带下载路径)")
    @PostMapping("v1/exportInventoryExcel")
    public ComResponse<List<InventoryProductExcelVo>> exportInventoryExcel(@RequestBody InventoryExcelVo inventoryExcelVo,HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<InventoryProductExcelVo>> listComResponse = inventoryFeginService.exportInventoryExcel(inventoryExcelVo);
        if (listComResponse==null || listComResponse.getCode() != 200)
            return listComResponse;

//        WriteCellStyle writeCellStyle = new WriteCellStyle();
//        WriteFont writeFont = new WriteFont();


        Integer status = inventoryExcelVo.getStatus();
        List<InventoryProductExcelVo> listComResponseData = listComResponse.getData();

        //盘点日期
        Date inventoryDate = inventoryExcelVo.getInventoryDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(inventoryDate);
//        String date = inventoryDate.toString();
        //仓库名称
//        String storeName = inventoryExcelVo.getStoreName();

        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式

        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+"PD"+date+".xlsx");


        //表头样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = ExcelStyleUtils.getHorizontalCellStyleStrategy();

        if (status==1){
            //向前端写入文件流流
            EasyExcel.write(httpServletResponse.getOutputStream(), InventoryProductExcelVo.class).registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet("盘点商品库存表").doWrite(listComResponseData);
        }else if (status==2 || status==3){

            List<InventoryProductResultExcelVo> inventoryProductResultExcelVoList = new ArrayList<>();
            for (InventoryProductExcelVo listComResponseDatum : listComResponseData) {
                InventoryProductResultExcelVo inventoryProductResultExcelVo = new InventoryProductResultExcelVo();
                BeanUtils.copyProperties(listComResponseDatum,inventoryProductResultExcelVo);
                inventoryProductResultExcelVoList.add(inventoryProductResultExcelVo);
            }
            //向前端写入文件流流
            EasyExcel.write(httpServletResponse.getOutputStream(), InventoryProductResultExcelVo.class).registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet("盘点商品库存表").doWrite(inventoryProductResultExcelVoList);
        }


        return ComResponse.success();
    }


    @ApiOperation(value = "导出库存",notes = "导出库存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeAndName", value = "商品编码、条形码或者商品名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "storeNo", value = "仓库编号", dataType = "string", paramType = "query")
    })
    @GetMapping("v1/exportProductStockExcel")
    public void exportProductStockExcel(@RequestParam("codeAndName") String codeAndName,@RequestParam("storeNo") String storeNo, HttpServletResponse httpServletResponse) throws IOException {
        downImageInService.exportProductStockExcel(codeAndName,storeNo,httpServletResponse);
    }


    @PostMapping(value = "v1/exportExcelOfProductPurchaseWarn")
    @ApiOperation("预警商品导出EXCEL")
    public void exportExcelOfProductPurchaseWarn(@RequestBody ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO,HttpServletResponse httpServletResponse) throws IOException {
        downImageInService.exportExcelOfProductPurchaseWarn(productPurchaseWarnExcelVO, httpServletResponse);
    }


    @PostMapping(value = "v1/exportExpressPrintInfo")
    @ApiOperation("导出快递模板")
    public void exportExpressPrintInfo(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo,
                                       HttpServletResponse httpServletResponse) throws IOException {
        downImageInService.exportExpressPrintInfo(outStoreOrderInfoParamVo, httpServletResponse);
    }

    @PostMapping(value = "v1/expressExportCount")
    @ApiOperation("导出快递数量统计")
    @ResponseBody
    public ComResponse getExpressExportCount(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo) {
        return downImageInService.getExpressExportCount(outStoreOrderInfoParamVo);
    }


}

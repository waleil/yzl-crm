package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.store.InventoryFeginService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.model.vo.InventoryExcelVo;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

//    @Value("${supplierDown.url}")
//    private String baseUrl;

    @ApiOperation("下载图片")
    @GetMapping("v1/downImage")
    public void downImage(@RequestParam("imageUrl") String imageUrl, HttpServletResponse httpServletResponse) throws IOException {
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = fastdfsUtils.download(imageUrl, null);
            String[] split = imageUrl.split("[.]");
            String[] splitPath = split[0].split("/");
            httpServletResponse.setContentType("image/" + split[split.length - 1]);
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" + splitPath[splitPath.length - 1]+"."+split[split.length - 1]);
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
    public ComResponse exportInventoryExcel(@RequestBody InventoryExcelVo inventoryExcelVo,HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<InventoryProductExcelVo>> listComResponse = inventoryFeginService.exportInventoryExcel(inventoryExcelVo);
        if (listComResponse==null || listComResponse.getCode() != 200)
            return listComResponse;

        List<InventoryProductExcelVo> listComResponseData = listComResponse.getData();

        //盘点日期
        Date inventoryDate = inventoryExcelVo.getInventoryDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(inventoryDate);
//        String date = inventoryDate.toString();
        //仓库名称
        String storeName = inventoryExcelVo.getStoreName();
        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" + storeName+ date+".xlsx");
//
        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), InventoryProductExcelVo.class)
                .sheet("盘点商品库存表").doWrite(listComResponseData);

        return ComResponse.success();
    }



}

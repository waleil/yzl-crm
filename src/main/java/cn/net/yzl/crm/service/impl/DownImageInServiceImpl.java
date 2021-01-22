package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.ProductStockFeignService;
import cn.net.yzl.crm.service.DownImageInService;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    @Override
    public ComResponse exportProductStockExcel(String codeAndName, String storeNo, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ProductStockExcelVo>> listComResponse = productStockFeignService.selectProductStockExcel(codeAndName, storeNo);

        if (listComResponse==null || listComResponse.getCode() !=200)
            return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(),ResponseCodeEnums.BIZ_ERROR_CODE.getMessage());
        List<ProductStockExcelVo> listComResponseData = listComResponse.getData();
        if (listComResponseData == null || listComResponseData.size()==0)
            return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(),ResponseCodeEnums.BIZ_ERROR_CODE.getMessage());

        //系统时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sysDate = simpleDateFormat.format(date);

        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=stock" + date+".xlsx");



        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), ProductStockExcelVo.class)
                .sheet("库存表").doWrite(listComResponseData);

        return ComResponse.success();
    }
}

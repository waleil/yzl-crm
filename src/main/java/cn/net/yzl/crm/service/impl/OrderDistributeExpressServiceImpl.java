package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.OrderDistributeExpressFeignService;
import cn.net.yzl.crm.controller.store.listen.ExpressExcelListener;
import cn.net.yzl.crm.service.OrderDistributeExpressService;
import cn.net.yzl.model.dto.express.ExpressImportModel;
import cn.net.yzl.model.vo.InventoryProductVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderDistributeExpressServiceImpl implements OrderDistributeExpressService {

    @Autowired
    private OrderDistributeExpressFeignService orderDistributeExpressFeignService;

    @Override
    public ComResponse readExpressExcelInfo(MultipartFile file) {

        //创建监听器
        ExpressExcelListener expressExcelListener = new ExpressExcelListener();
        ExcelReader excelReader = null;
        try {
            //读取excel
            excelReader = EasyExcel.read(file.getInputStream(), InventoryProductVo.class, expressExcelListener).build();
            excelReader.readAll();
            //获取错误信息
            Map<String, String> errorMessageMap = expressExcelListener.getErrorMessageMap();
            if (!CollectionUtils.isEmpty(errorMessageMap)){
                return ComResponse.fail(ResponseCodeEnums.EXCEL_HEAD_ERROR.getCode(),errorMessageMap.get("msg"));
            }
            //获取数据
            List<ExpressImportModel> expressImportModels = expressExcelListener.getExpressImportModels();
            log.info("导入数据解析结果:{}",expressImportModels);
            if (CollectionUtils.isEmpty(expressImportModels))
                return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE.getCode(),ResponseCodeEnums.NO_DATA_CODE.getMessage());

            return orderDistributeExpressFeignService.readExpressExcelInfo(expressImportModels);

        } catch (IOException e) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getCode(),ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getMessage());
        }

    }
}

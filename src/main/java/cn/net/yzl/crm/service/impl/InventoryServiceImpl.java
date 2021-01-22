package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.InventoryFeginService;
import cn.net.yzl.crm.controller.store.listen.InventoryExcelListener;
import cn.net.yzl.crm.service.InventoryService;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.InventoryExcelVo;
import cn.net.yzl.model.vo.InventoryProductVo;
import cn.net.yzl.model.vo.ReadExcelInventoryProductVo;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/19 9:17
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryFeginService inventoryFeginService;

    /**
     * 读取excel
     *
     * @param readExcelInventoryProductVo
     * @return
     */
    @Override
    public ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(ReadExcelInventoryProductVo readExcelInventoryProductVo,MultipartFile file) {
        if (readExcelInventoryProductVo == null || file==null)
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());

        //创建监听器
        InventoryExcelListener inventoryExcelListener = new InventoryExcelListener();
        ExcelReader excelReader = null;
        try {
            //读取excel
            excelReader = EasyExcel.read(file.getInputStream(), InventoryProductVo.class, inventoryExcelListener).build();
            excelReader.readAll();
            //获取数据
            List<InventoryProductVo> inventoryProductExcelVoList = inventoryExcelListener.getInventoryProductExcelVoList();
            if (inventoryProductExcelVoList == null || inventoryProductExcelVoList.size()==0)
                return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE.getCode(),ResponseCodeEnums.NO_DATA_CODE.getMessage());

            InventoryExcelVo inventoryExcelVo = new InventoryExcelVo();
            inventoryExcelVo.setId(readExcelInventoryProductVo.getId());
            inventoryExcelVo.setStoreNo(readExcelInventoryProductVo.getStoreNo());
            inventoryExcelVo.setStoreName(readExcelInventoryProductVo.getStoreName());
            inventoryExcelVo.setInventoryDate(readExcelInventoryProductVo.getInventoryDate());
            inventoryExcelVo.setInventoryProductVoList(inventoryProductExcelVoList);
            return inventoryFeginService.readExcelnventoryProduct(inventoryExcelVo);

        } catch (IOException e) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getCode(),ResponseCodeEnums.PARAMS_TYPE_ERROR_CODE.getMessage());
        }

    }

}

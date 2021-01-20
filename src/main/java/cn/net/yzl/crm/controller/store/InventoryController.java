package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.InventoryFeginService;
import cn.net.yzl.crm.controller.store.listen.InventoryExcelListener;
import cn.net.yzl.crm.service.InventoryService;
import cn.net.yzl.model.dto.InventoryDto;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 23:11
 */
@RestController
@Api
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryFeginService inventoryFeginService;


    @ApiOperation(value = "查询盘点列表", notes = "查询盘点列表")
    @PostMapping("v1/selectInventoryListPage")
    public ComResponse<Page<InventoryDto>> selectInventoryListPage(@RequestBody InventoryParamVo inventoryParamVo){
        return inventoryFeginService.selectInventoryListPage(inventoryParamVo);
    }

    @ApiOperation(value = "新增盘点",notes = "新增盘点")
    @PostMapping("v1/insertInventory")
    public ComResponse insertInventory(@RequestBody InventoryVo inventoryVo){
        return inventoryFeginService.insertInventory(inventoryVo);
    }

    @ApiOperation(value = "查询盘点商品信息",notes = "查询盘点商品信息")
    @PostMapping("v1/selectInventoryProduct")
    public ComResponse<Page<InventoryProductDto>> selectInventoryProduct(@RequestBody InventoryParamVo inventoryParamVo){
        return inventoryFeginService.selectInventoryProduct(inventoryParamVo);
    }

    @ApiOperation(value = "导出查看盘点商品(附带下载路径)",notes = "导出查看盘点商品(附带下载路径)")
    @PostMapping("v1/exportInventoryExcel")
    public ComResponse<String> exportInventoryExcel(@RequestBody InventoryExcelVo inventoryExcelVo){
        return inventoryFeginService.exportInventoryExcel(inventoryExcelVo);
    }

    @ApiOperation(value = "导入查看盘点商品",notes = "导入查看盘点商品信息")
    @PostMapping("v1/readExcelnventoryProduct")
    public ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(ReadExcelInventoryProductVo readExcelInventoryProductVo) throws IOException {
      return inventoryService.readExcelnventoryProduct(readExcelInventoryProductVo);
    }



}

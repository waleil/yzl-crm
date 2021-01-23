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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 23:11
 */
@RestController
@Api(value = "仓储中心心心心心-盘点管理", tags = {"仓储中心心心心心-盘点管理"})
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


    @ApiOperation(value = "导入查看盘点商品",notes = "导入查看盘点商品信息")
    @PostMapping("v1/readExcelnventoryProduct")
    public ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(@RequestParam("file") MultipartFile file,ReadExcelInventoryProductVo readExcelInventoryProductVo) throws IOException, ParseException {
//        ReadExcelInventoryProductVo readExcelInventoryProductVo = new ReadExcelInventoryProductVo();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date parse = sdf.parse(inventoryDate);
//        readExcelInventoryProductVo.setId(id);
//        readExcelInventoryProductVo.setStoreNo(storeNo);
//        readExcelInventoryProductVo.setStoreName(storeName);
//        readExcelInventoryProductVo.setInventoryDate(parse);
        return inventoryService.readExcelnventoryProduct(readExcelInventoryProductVo,file);
    }

    @ApiOperation(value = "修改盘点商品库存数据",notes = "修改盘点商品库存数据")
    @PostMapping("v1/updateInventoryProduct")
    public ComResponse updateInventoryProduct(@RequestBody InventoryAllProductVo inventoryAllProductVo){
        return inventoryFeginService.updateInventoryProduct(inventoryAllProductVo);
    }



}

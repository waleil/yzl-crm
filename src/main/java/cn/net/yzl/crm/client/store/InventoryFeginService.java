package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.InventoryDto;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/19 9:52
 */
//@FeignClient(name = "inventoryClient",url = "${api.gateway.url}/storeServer")
@FeignClient("yzl-store-server")
public interface InventoryFeginService {



    @ApiOperation(value = "查询盘点列表", notes = "查询盘点列表")
    @PostMapping("inventory/v1/selectInventoryListPage")
    ComResponse<Page<InventoryDto>> selectInventoryListPage(@RequestBody InventoryParamVo inventoryParamVo);

    @ApiOperation(value = "新增盘点",notes = "新增盘点")
    @PostMapping("inventory/v1/insertInventory")
    ComResponse insertInventory(@RequestBody InventoryVo inventoryVo);

    @ApiOperation(value = "查询盘点商品信息",notes = "查询盘点商品信息")
    @PostMapping("inventory/v1/selectInventoryProduct")
    ComResponse<Page<InventoryProductDto>> selectInventoryProduct(@RequestBody InventoryParamVo inventoryParamVo);

    @ApiOperation(value = "导出查看盘点商品",notes = "导出查看盘点商品信息")
    @PostMapping("inventory/v1/exportInventoryExcel")
    ComResponse<List<InventoryProductExcelVo>> exportInventoryExcel(@RequestBody InventoryExcelVo inventoryExcelVo);

    @ApiOperation(value = "导入查看盘点商品",notes = "导入查看盘点商品信息")
    @PostMapping("inventory/v1/readExcelnventoryProduct")
    ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(@RequestBody InventoryExcelVo inventoryExcelVo);

    @ApiOperation(value = "修改盘点商品库存数据",notes = "修改盘点商品库存数据")
    @PostMapping("inventory/v1/updateInventoryProduct")
     ComResponse updateInventoryProduct(@RequestBody InventoryAllProductVo inventoryAllProductVo);

}

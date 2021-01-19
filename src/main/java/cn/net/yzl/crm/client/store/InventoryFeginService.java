package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.InventoryExcelVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/19 9:52
 */
@FeignClient(name = "inventoryClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface InventoryFeginService {

    @ApiOperation(value = "导入查看盘点商品",notes = "导入查看盘点商品信息")
    @PostMapping("inventory/v1/readExcelnventoryProduct")
    ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(@RequestBody InventoryExcelVo inventoryExcelVo);

}

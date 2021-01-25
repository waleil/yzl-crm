package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.RemoveStockDto;
import cn.net.yzl.model.vo.RemoveStockRaramsVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/25 21:37
 */
@FeignClient(name = "removeStockClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface RemoveStockFeignService {

    @ApiOperation(value = "分页查询出库制表单表", notes = "分页查询出库制表单表")
    @PostMapping("removestock/v1/selectRemoveStoreListPage")
    ComResponse<Page<RemoveStockDto>> selectRemoveStoreListPage(@RequestBody RemoveStockRaramsVo removeStockRaramsVo);

}

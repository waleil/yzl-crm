package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.RemoveStockDto;
import cn.net.yzl.model.dto.RemoveStockManageDto;
import cn.net.yzl.model.vo.OutStoreOrderInfoParamVo;
import cn.net.yzl.model.vo.OutStoreOrderParamVo;
import cn.net.yzl.model.vo.OutStoreOrderVo;
import cn.net.yzl.model.vo.RemoveStockRaramsVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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


    @ApiOperation(value = "生成出库单",notes = "生成出库单")
    @PostMapping("removestock/v1/createOutStoreOrder")
    public ComResponse createOutStoreOrder(@RequestBody List<OutStoreOrderVo> outStoreOrderVoList);

    @ApiOperation(value = "查询出库单列表",notes = "查询出库单列表")
    @PostMapping("removestock/v1/selectOutStoreOrder")
    public ComResponse<Page<RemoveStockManageDto>> selectOutStoreOrder(@RequestBody OutStoreOrderParamVo outStoreOrderParamVo);

    @ApiOperation(value = "查看出库单详情",notes = "查看出库单详情")
    @PostMapping("removestock/v1/selectOutStoreOrderInfo")
    public ComResponse<Page<RemoveStockDto>> selectOutStoreOrderInfo(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo);

}

package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.vo.WarehousingSelectVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 11:09
 */
@FeignClient(name = "WarehousingClient",url = "${api.gateway.url}/storeServer")
public interface WarehousingFeignService {

    /**
     * 入库单下拉框
     * @param purchaseOrderNo
     * @return
     */
    @GetMapping("warehousing/v1/ware/order/no/list")
    @ApiOperation(value = "入库单下拉框", notes = "入库单下拉框")
     ComResponse<List<WarehousingSelectVo>> selectWareOrderNo(@RequestParam("purchaseOrderNo") String purchaseOrderNo);


}

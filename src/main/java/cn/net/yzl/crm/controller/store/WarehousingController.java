package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.store.WarehousingFeignService;
import cn.net.yzl.model.vo.WarehousingSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wujianing
 * @version 1.0
 * @date: 2021/1/18 21:43
 * @title: ProductStockDetailController
 * @description:
 */
@RestController
@Slf4j
@Api(value = "仓储中心-入库单", tags = {"仓储中心-入库单"})
@RequestMapping("warehousing")
public class WarehousingController {

    @Autowired
    private WarehousingFeignService warehousingFeignService;

    @ApiOperation(value = "入库单下拉框", notes = "入库单下拉框")
    @GetMapping("v1/ware/order/no/list")
    public ComResponse<List<WarehousingSelectVo>> selectWareOrderNo(@RequestParam("purchaseOrderNo") String purchaseOrderNo){
        return warehousingFeignService.selectWareOrderNo(purchaseOrderNo);
    }


}

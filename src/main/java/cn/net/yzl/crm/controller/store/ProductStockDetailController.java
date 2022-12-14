package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.ProductStockDetailFeignService;
import cn.net.yzl.model.dto.ProductStockDetailDTO;
import cn.net.yzl.model.pojo.StockDetailType;
import cn.net.yzl.model.vo.ProductStockDetailSelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "仓储中心心心心心-库存流水", tags = {"仓储中心心心心心-库存流水"})
@RequestMapping("productStockDetail")
public class ProductStockDetailController {

    @Autowired
    ProductStockDetailFeignService productStockDetailFeignService;


    @PostMapping(value = "v1/selectProductStockDetail")
    @ApiOperation("库存流水分页查询")
    public ComResponse<Page<ProductStockDetailDTO>> selectProductStockDetail(@RequestBody ProductStockDetailSelectVO productStockDetailSelectVO) {
        return productStockDetailFeignService.selectProductStockDetail(productStockDetailSelectVO);
    }

    @GetMapping(value = "v1/selectStockDetailTypeList")
    @ApiOperation("库存流水类型列表查询")
    public ComResponse<List<StockDetailType>> selectStockDetailTypeList() {
        return productStockDetailFeignService.selectStockDetailTypeList();
    }

}

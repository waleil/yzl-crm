package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.ProductPurchaseWarnFeignService;
import cn.net.yzl.model.dto.ProductPurchaseWarnDTO;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.dto.ProductPurchaseWarnSetDTO;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductPurchaseWarnSetVO;
import cn.net.yzl.model.vo.ProductPurchaseWarnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 16:00
 */
@RestController
@Api(value = "仓储中心心心心心-商品采购预警", tags = {"仓储中心心心心心-商品采购预警"})
@RequestMapping("productPurchaseWarn")
public class ProductPurchaseWarnController {

    @Autowired
    private ProductPurchaseWarnFeignService feignService;

    @GetMapping(value = "v1/selectProductPurchaseWarnSetList")
    @ApiOperation("查询预警通知设置列表")
    public ComResponse<List<ProductPurchaseWarnSetDTO>> selectProductPurchaseWarnSetList() {
        return feignService.selectProductPurchaseWarnSetList();
    }

    @GetMapping(value = "v1/selectProductPurchaseWarnSet")
    @ApiOperation("查询预警通知设置")
    public ComResponse<ProductPurchaseWarnSetDTO> selectProductPurchaseWarnSet(@RequestParam("storeNo") String storeNo) {
        return feignService.selectProductPurchaseWarnSet(storeNo);
    }

    @PostMapping(value = "v1/updateProductPurchaseWarnSet")
    @ApiOperation("修改预警通知设置")
    public ComResponse updateProductPurchaseWarnSet(@RequestBody ProductPurchaseWarnSetVO productPurchaseWarnSetVO) {
        return feignService.updateProductPurchaseWarnSet(productPurchaseWarnSetVO);
    }

    @PostMapping(value = "v1/selectProductPurchaseWarnList")
    @ApiOperation("采购商品预警列表分页查询")
    public ComResponse<Page<ProductPurchaseWarnDTO>> selectProductPurchaseWarnList(@RequestBody ProductPurchaseWarnVO productPurchaseWarnVO) {
        return feignService.selectProductPurchaseWarnList(productPurchaseWarnVO);
    }

    @PostMapping(value = "v1/insertOrderDataToProductPurchaseWarn")
    @ApiOperation("获取订单服务数据计算并保存")
    public ComResponse insertOrderDataToProductPurchaseWarn() {
        return feignService.insertOrderDataToProductPurchaseWarn();
    }

    @PostMapping(value = "v1/selectExcelOfProductPurchaseWarn")
    @ApiOperation("预警商品导出EXCEL")
    public ComResponse<List<ProductPurchaseWarnExcelDTO>> selectExcelOfProductPurchaseWarn(@RequestBody ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO) {
        return feignService.selectExcelOfProductPurchaseWarn(productPurchaseWarnExcelVO);
    }
}

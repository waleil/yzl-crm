package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.ProductPurchaseWarnDTO;
import cn.net.yzl.model.dto.ProductPurchaseWarnExcelDTO;
import cn.net.yzl.model.dto.ProductPurchaseWarnSetDTO;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductPurchaseWarnSetVO;
import cn.net.yzl.model.vo.ProductPurchaseWarnVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 14:50
 */
@FeignClient(name = "purchaseWarnClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface ProductPurchaseWarnFeignService {

    @GetMapping(value = "productPurchaseWarn/v1/selectProductPurchaseWarnSetList")
    @ApiOperation("查询预警通知设置列表")
    public ComResponse<List<ProductPurchaseWarnSetDTO>> selectProductPurchaseWarnSetList();

    @GetMapping(value = "productPurchaseWarn/v1/selectProductPurchaseWarnSet")
    @ApiOperation("查询预警通知设置")
    public ComResponse<ProductPurchaseWarnSetDTO> selectProductPurchaseWarnSet(@RequestParam("storeNo") String storeNo);

    @PostMapping(value = "productPurchaseWarn/v1/updateProductPurchaseWarnSet")
    @ApiOperation("修改预警通知设置")
    public ComResponse updateProductPurchaseWarnSet(@RequestBody ProductPurchaseWarnSetVO productPurchaseWarnSetVO);

    @PostMapping(value = "productPurchaseWarn/v1/selectProductPurchaseWarnList")
    @ApiOperation("采购商品预警列表分页查询")
    public ComResponse<Page<ProductPurchaseWarnDTO>> selectProductPurchaseWarnList(@RequestBody ProductPurchaseWarnVO productPurchaseWarnVO);

    @PostMapping(value = "productPurchaseWarn/v1/insertOrderDataToProductPurchaseWarn")
    @ApiOperation("获取订单服务数据计算并保存")
    public ComResponse insertOrderDataToProductPurchaseWarn();

    @PostMapping(value = "productPurchaseWarn/v1/selectExcelOfProductPurchaseWarn")
    @ApiOperation("预警商品导出EXCEL")
    public ComResponse<List<ProductPurchaseWarnExcelDTO>> selectExcelOfProductPurchaseWarn(@RequestBody ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO);
}

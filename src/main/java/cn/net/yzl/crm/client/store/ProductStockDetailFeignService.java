package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.ProductStockDetailDTO;
import cn.net.yzl.model.pojo.StockDetailType;
import cn.net.yzl.model.vo.ProductStockDetailSelectVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/21 14:04
 */
@FeignClient(name = "productStockDetailClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface ProductStockDetailFeignService {

    @PostMapping(value = "productStockDetail/v1/selectProductStockDetail")
    @ApiOperation("库存流水分页查询")
    public ComResponse<Page<ProductStockDetailDTO>> selectProductStockDetail(@RequestBody ProductStockDetailSelectVO productStockDetailSelectVO);

    @GetMapping(value = "productStockDetail/v1/selectStockDetailTypeList")
    @ApiOperation("库存流水类型列表查询")
    public ComResponse<List<StockDetailType>> selectStockDetailTypeList();
}

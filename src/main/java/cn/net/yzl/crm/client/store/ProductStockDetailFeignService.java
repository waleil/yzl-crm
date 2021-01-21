package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.ProductStockDetailDTO;
import cn.net.yzl.model.vo.ProductStockDetailSelectVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/21 14:04
 */
@FeignClient(name = "productStockDetailClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface ProductStockDetailFeignService {

    @PostMapping(value = "store/v1/selectProductStockDetail")
    @ApiOperation("库存流水分页查询")
    public ComResponse<Page<ProductStockDetailDTO>> selectProductStockDetail(@RequestBody ProductStockDetailSelectVO productStockDetailSelectVO);
}

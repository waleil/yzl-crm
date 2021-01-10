package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductAtlasDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDetailVO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "productClient", url = "${api.gateway.url}/productServer/product/v1")
public interface ProductClient {

    @GetMapping(value = "queryCountByStatus")
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

    @GetMapping(value = "queryPageProduct")
    ComResponse<Page<ProductListDTO>> queryListProduct(@RequestParam("vo") ProductSelectVO vo);

    @PostMapping(value = "edit")
    ComResponse<Void> editProduct(@RequestBody ProductVO vo);

    @PostMapping(value = "updateStatus")
    ComResponse updateStatusByProductCode(@RequestBody @Valid ProductUpdateStatusVO vo);

    @GetMapping("queryProductListAtlas")
    ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(@RequestParam("productName") String productName, @RequestParam("id") Integer id,@RequestParam("pid") Integer pid);

    @PostMapping(value = "updateTime")
    ComResponse updateTimeByProductCode(@RequestBody ProductUpdateTimeVO vo);

    @GetMapping(value = "queryProductDetail")
    @ApiOperation("查询商品详情")
    ComResponse<ProductDetailVO> queryProductDetail(@RequestParam("productCode") String productCode);
}

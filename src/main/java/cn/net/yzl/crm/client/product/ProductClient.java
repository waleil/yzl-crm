package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.*;
import cn.net.yzl.product.model.vo.product.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "productClient", url = "localhost:2077/product")
public interface ProductClient {

    @GetMapping(value = "v1/queryCountByStatus")
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

    @PostMapping(value = "/v1/queryPageProduct")
    ComResponse<Page<ProductListDTO>> queryListProduct(@RequestBody ProductSelectVO vo);

    @PostMapping(value = "v1/edit")
    ComResponse<Void> editProduct(@RequestBody ProductVO vo);

    @PostMapping(value = "v1/updateStatus")
    ComResponse updateStatusByProductCode(@RequestBody ProductUpdateStatusVO vo);

    @GetMapping("v1/queryProductListAtlas")
    ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(@RequestParam("productName") String productName, @RequestParam("id") Integer id,@RequestParam("pid") Integer pid);

    @PostMapping(value = "v1/updateTime")
    ComResponse updateTimeByProductCode(@RequestBody ProductUpdateTimeVO vo);

    @GetMapping(value = "v1/queryProductDetail")
    ComResponse<ProductDetailVO> queryProductDetail(@RequestParam("productCode") String productCode);

    @GetMapping(value = "v1/queryProductPortrait")
     ComResponse<ProductPortraitDTO> queryProductPortrait(@RequestParam("productCode") String productCode);
    @GetMapping(value = "v1/queryDiseaseByProductCode")
    ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(@RequestParam("productCode") String productCode);
}

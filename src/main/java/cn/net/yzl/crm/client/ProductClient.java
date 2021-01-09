package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductAtlasDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateStatusVO;
import cn.net.yzl.product.model.vo.product.vo.ProductVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "productClient", url = "${api.gateway.url}/productServer/product")
public interface ProductClient {

    @GetMapping(value = "v1/queryCountByStatus")
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

    @GetMapping(value = "v1/queryPageProduct")
    ComResponse<Page<ProductListDTO>> queryListProduct(@RequestParam("vo") ProductSelectVO vo);

    @PostMapping(value = "v1/edit")
    ComResponse<Void> editProduct(@RequestBody ProductVO vo);

    @PostMapping(value = "v1/updateStatus")
    ComResponse updateStatusByProductCode(@RequestBody @Valid ProductUpdateStatusVO vo);
    @GetMapping("v1/queryProductListAtlas")
    ComResponse<ProductAtlasDTO> queryProductListAtlas(@RequestParam("productName") String productName, @RequestParam("id") Integer id);
}

package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "productClient",url = "http://127.0.0.1:2077/product")
public interface ProductClient {

    @GetMapping(value = "v1/queryCountByStatus")
     ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

    @GetMapping(value = "v1/queryPageProduct")
     ComResponse<Page<ProductListDTO>> queryListProduct(@RequestParam("vo") ProductSelectVO vo);

    @PostMapping(value = "v1/edit")
     ComResponse<Void> editProduct(@RequestBody  ProductVO vo);
}

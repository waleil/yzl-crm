package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.ProductMainInfoBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "productClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/product")
public interface ProductClient {

    @GetMapping("/v1/getMainInfoByIds")
    ComResponse<List<ProductMainInfoBean>> getMainInfoByIds(@RequestParam("ids") String ids, @RequestParam("status") Integer status);

}

package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/product")
public interface ProductClient {

    @DeleteMapping("/v1/product/deleteProductImgId")
    ComResponse<Void> deleteRelationOfProductAndImgId(@RequestParam("id")Integer id,
                                                      @RequestParam("type")Integer type);

}

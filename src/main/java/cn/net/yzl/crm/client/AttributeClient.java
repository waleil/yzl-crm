package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.AttributeBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "attributeClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/attribute")
public interface AttributeClient {

    @PostMapping("/v1/insert")
    ComResponse insertProductAttribute(@RequestBody AttributeBean attributeBean);

    @GetMapping("/v1/selectPage")
    ComResponse selectPageAttribute(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize);


    @GetMapping("/v1/selectById")
    ComResponse selectById(@RequestParam("id") Integer id);

    @GetMapping("/v1/selectByCid")
    ComResponse selectByclassifyIdAttribute(@RequestParam("id") Integer id);


    @PutMapping("/v1/update")
    ComResponse updateAttribute(@RequestBody AttributeBean attributeBean);

}
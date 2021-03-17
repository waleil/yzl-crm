package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.AttributeBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "attributeClient",url = "${api.gateway.url}/productServer/attribute/v1")
public interface AttributeClient {

//    @PostMapping("insert")
//    ComResponse<?> insertProductAttribute(@RequestBody AttributeBean attributeBean);
//
//    @GetMapping("selectPage")
//    ComResponse<?> selectPageAttribute(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize);
//
//
//    @GetMapping("selectById")
//    ComResponse<?> selectById(@RequestParam("id") Integer id);
//
//    @GetMapping("selectByCid")
//    ComResponse<?> selectByclassifyIdAttribute(@RequestParam("id") Integer id);
//
//
//    @PutMapping("update")
//    ComResponse<?> updateAttribute(@RequestBody AttributeBean attributeBean);

}
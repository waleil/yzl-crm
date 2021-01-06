package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.Image;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "imageClient",url = "http://api.staff.yuzhilin.net.cn/productServer/image")
public interface ImageClient {

    @PostMapping("/v1/insert")
    ComResponse insert(@RequestBody Image img);
}

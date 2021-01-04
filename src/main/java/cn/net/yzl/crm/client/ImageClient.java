package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "imageClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/image")
public interface ImageClient {

    @PostMapping("/v1/v1/image/insert")
    ComResponse<Integer> insertImage(@RequestParam("url")String url, @RequestParam("type") Integer type);

}

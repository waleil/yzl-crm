package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(name = "imageClient",url = "${api.gateway.url}/productServer/image")
public interface ImageClient {

    @PostMapping("/v1/insert")
    ComResponse insert(@RequestBody Image img);

    @PostMapping("/v1/createAlbum")
    ComResponse createAlbum(ImageStore is);

    @GetMapping("v1/selectByStoreId")
    ComResponse  selectByStoreId(@RequestParam("id") Integer id);
}

package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.image.ImageVO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "imageClient",url = "${api.gateway.url}/productServer/image/v1")
public interface ImageClient {

    @PostMapping("insert")
    ComResponse insert(@RequestBody ImageVO img);

    @PostMapping("createAlbum")
    ComResponse createAlbum(@RequestBody ImageStoreVO is);

    @GetMapping("selectByStoreId")
    ComResponse<Page<ImageDTO>>  selectByStoreId(@RequestParam("id") Integer id, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("selectTypeById")
    ComResponse selectTypeById(@RequestParam("id") Integer id );

    @GetMapping("selectStores")
    ComResponse<List<ImageStoreDTO>> selectStores(@RequestParam("type") Integer type);

    @GetMapping("deleteById")
    ComResponse deleteById(@RequestParam("id") Integer id,@RequestParam("userId") String userId);

    @GetMapping("deleteStoreById")
    ComResponse deleteStoreById(@RequestParam("id") Integer id,@RequestParam("userId") String userId);
}

package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.StoreDto;
import cn.net.yzl.model.dto.StoreLocalDto;
import cn.net.yzl.model.pojo.StorePo;
import cn.net.yzl.model.vo.StoreLocalVo;
import cn.net.yzl.model.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/16 13:37
 */
//@FeignClient("yzl-store-server")
@FeignClient(name = "storeClient",url = "${api.gateway.url}/storeServer")
public interface StoreFeginService {

    @GetMapping("store/v1/selectStoreListPage")
    public ComResponse<Page<StorePo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "storeNo",required = false) String storeNo,
                                                          @RequestParam(value = "sType",required = false) Integer sType,
                                                          @RequestParam(value = "mType",required = false) Integer mType);


    @GetMapping("store/v1/selectStore")
    public ComResponse<StoreDto> selectStore(@RequestParam("id") Integer id);



    @PostMapping("store/v1/insertStore")
    public ComResponse insertStore(@RequestBody StoreVO storeVO);


    @GetMapping("store/v1/updateStoreStatus")
    public ComResponse updateStoreStatus(@RequestParam("id") Integer id,@RequestParam("status") Integer status);


    @PostMapping("store/v1/insertStoreLocal")
    public ComResponse insertStoreLocal(@RequestBody StoreLocalVo storeLocalVo);



    @GetMapping("store/v1/selectStoreLocalInfo")
    public ComResponse<StoreLocalDto> selectStoreLocalInfo(@RequestParam("id") Integer id);

    @PostMapping("store/v1/updateStoreLocal")
    public ComResponse updateStoreLocal(@RequestBody StoreLocalVo storeLocalVo);

}
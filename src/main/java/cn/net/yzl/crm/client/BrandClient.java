package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.product.model.vo.bread.BrandVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "brandClient",url = "http://api.staff.yuzhilin.net.cn/productServer/brand")
public interface BrandClient {

    @ApiOperation(value = "获取所有品牌信息")
    @GetMapping("/v1/getPage")
    ComResponse getAllBrands(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam("keyWord") String keyword);

    @GetMapping("/v1/selectById")
    ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id);

    @PutMapping("/v1/changeStatus")
    ComResponse changeBrandStatus(@RequestParam("flag") Integer flag, @RequestParam("id") Integer id);

    @PostMapping("/v1/edit")
    ComResponse editBrand(@RequestBody BrandVO brand);

    @DeleteMapping("/v1/deleteById")
    void deleteById(Integer id);
}
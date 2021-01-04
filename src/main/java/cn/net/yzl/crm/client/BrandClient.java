package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.BrandBean;
import cn.net.yzl.crm.model.BrandBeanTO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "brandClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/brand")
public interface BrandClient {
    @ApiOperation(value = "获取所有品牌信息")
    @GetMapping("/v1/brand/selectAll")
    ComResponse<PageInfo<BrandBeanTO>> getAllBrands(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("/v1/brand/selectById")
    ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id);

    @GetMapping("/v1/brand/selectByBid")
    ComResponse<List<BrandBean>> getProductByBid(@RequestParam("bid") Integer bid);

    @PutMapping("/v1/brand/changeStatus")
    ComResponse<Void> changeBrandStatus(@RequestParam("flag") Integer flag, @RequestParam("id") Integer id);

    @PostMapping("/v1/brand/insert")
    ComResponse insertBrand(@RequestBody BrandBean brand);

    @PutMapping("/v1/brand/update")
    ComResponse<Void> updateBrand(@RequestBody BrandBean brandBean);
}
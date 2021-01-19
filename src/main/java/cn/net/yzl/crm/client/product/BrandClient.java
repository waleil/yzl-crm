package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.BrandBean;
import cn.net.yzl.product.model.vo.brand.BrandBeanTO;
import cn.net.yzl.product.model.vo.brand.BrandDelVO;
import cn.net.yzl.product.model.vo.brand.BrandVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "brandClient",url = "${api.gateway.url}/productServer/brand/v1")
public interface BrandClient {

    @ApiOperation(value = "获取所有品牌信息")
    @GetMapping("getPage")
    ComResponse getAllBrands(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam("keyWord") String keyword);

    @GetMapping("selectById")
    ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id);

    @PutMapping("changeStatus")
    ComResponse changeBrandStatus(@RequestParam("flag") Integer flag, @RequestParam("id") Integer id);

    @PostMapping("edit")
    ComResponse editBrand(@RequestBody BrandVO brand);

    @PostMapping("deleteById")
    ComResponse deleteById(@RequestBody BrandDelVO brandDelVO);

    @GetMapping("checkUnique")
    ComResponse<Boolean> checkUnique(@RequestParam("name") String name, @RequestParam("id") int id);

    @GetMapping("query4Select")
    ComResponse<List<BrandBeanTO>> query4Select();
}
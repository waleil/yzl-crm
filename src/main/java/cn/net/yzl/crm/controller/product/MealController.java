package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.product.MealService;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product/v1/meal")
@Api("套餐服务")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping(value = "v1/queryCountByStatus")
    @ApiOperation("按照上下架状态查询商品套餐数量")
    public ComResponse<List<ProductStatusCountDTO>> queryCountByStatus() {
        List<ProductStatusCountDTO> list = mealService.queryCountByStatus();
        return ComResponse.success(list);
    }

}

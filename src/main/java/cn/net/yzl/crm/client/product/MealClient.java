package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.db.Meal;
import cn.net.yzl.product.model.vo.product.dto.MealDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "mealClient",url = "${api.gateway.url}/productServer/productMeal")
//@FeignClient("yzl-product-server")
public interface MealClient {



    /**
     * @param
     * @Author: wanghuasheng
     * @Description:
     * @Date: 2021/1/9 09:20 上午
     * @Return: cn.net.yzl.common.entity.ComResponse<java.util.List < cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO>>
     */
    @GetMapping(value = "v1/queryCountByStatus")
    @ApiOperation("按照上下架状态查询商品套餐数量")
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();


    @GetMapping(value = "v1/queryPageProductMeal")
    @ApiOperation("分页查询商品套餐列表")
    ComResponse<Page<ProductMealListDTO>> queryListProductMeal(@SpringQueryMap ProductMealSelectVO vo);
    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:12 上午
     * @param：
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateStatus")
    @ApiOperation("修改套餐上下架状态")

    ComResponse updateStatusByMealCode(@RequestBody @Valid ProductMealUpdateStatusVO vo);

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:30
     * @param :
     * @param vo
     * @return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     **/
    @PostMapping(value = "v1/edit")
    @ApiOperation("编辑套餐")
    ComResponse<Void> editProductMeal(@RequestBody MealVO vo);



    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 13:00
     * @param:
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/queryMealDetail")
    @ApiOperation("查询商品详情")
    ComResponse<ProductMealDetailVO> queryMealDetail(@RequestBody Meal meal);

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 13:26
     * @param vo:
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateTime")
    @ApiOperation("修改套餐售卖时间")
    ComResponse updateTimeByMealCode(@RequestBody @Valid ProductMealUpdateTimeVO vo);

    @GetMapping(value = "v1/queryProductMealPortray")
    @ApiOperation("查询商品套餐画像")
    ComResponse<MealDTO> queryProductMealPortray(@RequestParam("mealNo") Integer mealNo);

}

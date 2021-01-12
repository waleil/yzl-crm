package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.service.product.MealService;
import cn.net.yzl.product.model.db.Meal;
import cn.net.yzl.product.model.vo.product.dto.MealDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.*;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("productMeal")
@Api(tags = "套餐服务")
public class MealController {

    @Autowired
    private MealClient mealClient;


    /**
     * @param
     * @Author: wanghuasheng
     * @Description:
     * @Date: 2021/1/9 09:20 上午
     * @Return: cn.net.yzl.common.entity.ComResponse<java.util.List < cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO>>
     */
    @GetMapping(value = "v1/queryCountByStatus")
    @ApiOperation("按照上下架状态查询商品套餐数量")
    public ComResponse<List<ProductStatusCountDTO>> queryCountByStatus() {
       return mealClient.queryCountByStatus();
    }


    @GetMapping(value = "v1/queryPageProductMeal")
    @ApiOperation("分页查询商品套餐列表")
    public ComResponse<Page<ProductMealListDTO>> queryListProductMeal(ProductMealSelectVO vo) {
        return mealClient.queryListProductMeal(vo);
    }
    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:12 上午
     * @param：
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateStatus")
    @ApiOperation("修改套餐上下架状态")

    public ComResponse updateStatusByMealCode(@RequestBody @Valid ProductMealUpdateStatusVO vo) {
        if (CollectionUtils.isEmpty(vo.getMealNoList())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), "套餐code不能为空");
        }
        return mealClient.updateStatusByMealCode(vo);
    }

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 11:30
     * @param :
     * @return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     **/
    @PostMapping(value = "v1/edit")
    @ApiOperation("编辑套餐")
    public ComResponse<Void> editProductMeal(@RequestBody @Valid MealVO vo) {
        return mealClient.editProductMeal(vo);
    }



//    /**
//     * @Description:
//     * @Author: dongjunmei
//     * @Date: 2021-01-09 13:00
//     * @param:
//     * @return: cn.net.yzl.common.entity.ComResponse
//     **/
//    @PostMapping(value = "v1/queryMealDetail")
//    @ApiOperation("查询商品详情")
//    public ComResponse<ProductMealDetailVO> queryMealDetail(@RequestBody Meal meal) {
//        if(meal.getMealNo()!=null){
//            return mealClient.queryMealDetail(meal);
//        }
//        return null;
//    }

    /**
     * @Description:
     * @Author: dongjunmei
     * @Date: 2021-01-09 13:26
     * @return: cn.net.yzl.common.entity.ComResponse
     **/
    @PostMapping(value = "v1/updateTime")
    @ApiOperation("修改套餐售卖时间")
    ComResponse updateTimeByMealCode(@RequestBody @Valid ProductMealUpdateTimeVO vo) {

        return mealClient.updateTimeByMealCode(vo);
    }

    @GetMapping(value = "v1/queryProductMealPortray")
    @ApiOperation("查询商品套餐画像")
    public ComResponse<MealDTO> queryProductMealPortray(@RequestParam("mealNo") Integer mealNo) {
        return mealClient.queryProductMealPortray(mealNo);
    }
    

}

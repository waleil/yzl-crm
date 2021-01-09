package cn.net.yzl.crm.controller.product;

import cn.net.yzl.crm.service.product.MealService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/v1/meal")
@Api("套餐服务")
public class MealController {

    @Autowired
    private MealService mealService;

}

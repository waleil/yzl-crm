package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.service.product.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealClient mealClient;

}

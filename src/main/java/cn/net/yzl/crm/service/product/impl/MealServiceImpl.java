package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.service.product.MealService;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealClient mealClient;

    @Override
    public List<ProductStatusCountDTO> queryCountByStatus() {
        return mealClient.queryCountByStatus().getData();
    }
}

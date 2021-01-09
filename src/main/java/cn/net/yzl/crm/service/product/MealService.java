package cn.net.yzl.crm.service.product;

import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;

import java.util.List;

public interface MealService {
    List<ProductStatusCountDTO> queryCountByStatus();
}

package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "mealClient",url = "${api.gateway.url}/productServer/meal/v1")
public interface MealClient {

    @GetMapping(value = "queryCountByStatus")
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

}

package cn.net.yzl.crm.client.product;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "mealClient",url = "${api.gateway.url}/productServer/meal/v1")
public interface MealClient {

}

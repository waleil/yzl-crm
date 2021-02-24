package cn.net.yzl.crm.client.score;

import cn.net.yzl.crm.client.product.ProductClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "scoreProductClient", url = "${api.gateway.url}")
public interface ScoreProductClient {
}

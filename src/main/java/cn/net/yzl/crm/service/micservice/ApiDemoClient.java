package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = ApiDemoClient.NAME, url = ApiDemoClient.URL, path = ApiDemoClient.PATH)
public interface ApiDemoClient {
	final String NAME = "${spring.application.name}";
	final String PATH = "checkorder/order/";
	final String URL = "localhost:8083/";
	

	@PostMapping("/getById")
	public GeneralResult<Map<String,Object>> getById(@RequestParam("id") String id);
	

}
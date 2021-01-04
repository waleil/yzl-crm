package cn.net.yzl.crm.service.micservice;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * 媒介接口
 */
@FeignClient(value = "marketBaseDB",url = "${api.gateway.url}/marketBaseDB")
public interface CoopCompanyMediaFien {

}

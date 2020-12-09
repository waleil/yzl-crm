package cn.net.yzl.crm.service.micservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 19:07
 * @description:
 */
@FeignClient("user-service")
public interface UserService {
    @RequestMapping(method = RequestMethod.GET, value = "/user/getuser")
    String getUserName();
}

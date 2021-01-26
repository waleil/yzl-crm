package cn.net.yzl.crm.service.micservice.member;


import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "crmCustomerPhone", url = "localhost:2070/memberPhone")
//@FeignClient(name = "crmCustomer", url = "${api.gateway.url}/crmCustomer/memberPhone")
public interface MemberPhoneFien {


    @ApiOperation("顾客手机号-获取顾客会员号")
    @GetMapping("/v1/getMemberCard")
    ComResponse<String> getMemberCard(@RequestParam("phoneNumber") String phoneNumber);
}

package cn.net.yzl.crm.service.micservice.member;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.Member;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "crmCustomerPhone", url = "${api.gateway.url}/crmCustomer/memberPhone")
public interface MemberPhoneFien {



    @ApiOperation("顾客手机号-获取顾客会员")
    @GetMapping("/v1/getMemberByphoneNumber")
    ComResponse<Member> getMemberByphoneNumber(@RequestParam("phoneNumber") String phoneNumber);
}

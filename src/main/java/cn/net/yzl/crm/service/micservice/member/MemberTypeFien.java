package cn.net.yzl.crm.service.micservice.member;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberTypeDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "crmMemberType", url = "${api.gateway.url}/crmCustomer/memberType")
public interface MemberTypeFien {

    @ApiOperation("获取顾客服用效果记录")
    @GetMapping("/v1/queryMemberType")
    ComResponse<List<MemberTypeDTO>> queryMemberType();
}

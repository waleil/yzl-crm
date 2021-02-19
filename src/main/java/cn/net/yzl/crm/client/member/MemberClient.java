package cn.net.yzl.crm.client.member;

import cn.net.yzl.common.entity.ComResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "merberClient", url = "${api.gateway.url}/crmCustomer/member")
@Service
public interface MemberClient {
    /**
     * 根据一批顾客群组id获取群组信息,用英文逗号分隔
     * @param crowdGroupIds
     * @return
     */
    @GetMapping("/v1/getCrowdGroupList")
    public ComResponse getCrowdGroupList( @RequestParam("crowdGroupIds")String crowdGroupIds) ;


}
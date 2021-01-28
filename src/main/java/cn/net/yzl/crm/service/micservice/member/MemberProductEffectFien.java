package cn.net.yzl.crm.service.micservice.member;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberProductEffectDTO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "crmCustomerProductEffect", url = "${api.gateway.url}/crmCustomer/memberProductEffect")
public interface MemberProductEffectFien {

    @ApiOperation("修改顾客服用效果记录")
    @PostMapping("/v1/batchModifyProductEffect")
    ComResponse batchModifyProductEffect(@RequestBody List<MemberProductEffectUpdateVO> productEffectVOs);

    @ApiOperation("获取顾客服用效果记录")
    @PostMapping("/v1/getProductEffects")
    ComResponse<List<MemberProductEffectDTO>> getProductEffects(@RequestBody MemberProductEffectSelectVO productEffect);
}

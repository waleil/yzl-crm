package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.model.Member;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("yzl-crm-customer-server")
public interface MemberFien {
    @RequestMapping(method = RequestMethod.POST, value = "v1/getMemberListByPage")
    GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto);

    @PostMapping("v1/save")
    GeneralResult<Boolean> save(@RequestBody Member dto);

    @PostMapping("v1/updateByMemberCart")
    GeneralResult<Boolean> updateByMemberCart(@RequestBody Member dto);

    @GetMapping("v1/getMember")
    GeneralResult<Member> getMember(@RequestParam("memberCard") String  memberCard);

    @GetMapping("v1/getMemberGrad")
    GeneralResult getMemberGrad();
}

package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("yzl-crm-customer-api")
public interface MemberFien {
    @RequestMapping(method = RequestMethod.POST, value = "customer/member/listPage")
    GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto);
}

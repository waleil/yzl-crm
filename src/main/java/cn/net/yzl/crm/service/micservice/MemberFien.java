package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.model.Member;
import cn.net.yzl.crm.model.MemberGrade;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 顾客服务接口
 */
@FeignClient(name = "crmCustomer",url = "http://api.staff.yuzhilin.net.cn/crmCustomer")
//    @FeignClient(value = "yzl-crm-customer-api")
public interface MemberFien {

    @RequestMapping(method = RequestMethod.POST, value = "/member/v1/getMemberListByPage")
    GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto);

    @ApiOperation("保存会员信息")
    @PostMapping("/member/v1/save")
    GeneralResult<Boolean> save(@RequestBody Member dto);

    @ApiOperation("修改会员信息")
    @PostMapping("/member/v1/updateByMemberCart")
    GeneralResult<Boolean> updateByMemberCart(@RequestBody Member dto);

    @ApiOperation("根据卡号获取会员信息")
    @GetMapping("/member/v1/getMember")
    GeneralResult<Member> getMember(@RequestParam("memberCard") String  memberCard);

    @ApiOperation("获取会员等级")
    @GetMapping("/member/v1/getMemberGrad")
    GeneralResult<List<MemberGrade>> getMemberGrad();
}

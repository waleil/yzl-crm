package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 顾客服务接口
 */
@FeignClient(name = "crmCustomer",url = "http://api.staff.yuzhilin.net.cn/crmCustomer")
//@FeignClient(value = "yzl-crm-customer-api")
public interface MemberFien {

    @RequestMapping(method = RequestMethod.POST, value = "/member/v1/getMemberListByPage")
    GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto);

    @ApiOperation("保存会员信息")
    @PostMapping("/member/v1/save")
    GeneralResult<Boolean> save(@RequestBody Member dto);

    @ApiOperation("修改会员信息")
    @PostMapping("/member/v1/updateByMemberCard")
    GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto);

    @ApiOperation("根据卡号获取会员信息")
    @GetMapping("/member/v1/getMember")
    GeneralResult<Member> getMember(@RequestParam("memberCard") String  memberCard);

    @ApiOperation("获取会员等级")
    @GetMapping("/member/v1/getMemberGrad")
    GeneralResult<List<MemberGrad>> getMemberGrad();

    @ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
    @GetMapping("/member/v1/getMemberPhoneList")
    GeneralResult<List<MemberPhone>> getMemberPhoneList(String member_card);

    @ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
    @GetMapping("/member/v1/getMemberByPhone")
    GeneralResult<Member> getMemberByPhone(String phone);

    @ApiOperation("设置顾客为会员")
    @GetMapping("/member/v1/setMemberToVip")
    void   setMemberToVip(String member_card);

    @ApiOperation("获取顾客购买商品")
    @GetMapping("/member/v1/getMemberProductEffectList")
    GeneralResult<List<MemberProductEffect>> getMemberProductEffectList(String member_card);

    @ApiOperation("获取顾客咨询商品")
    @GetMapping("/member/v1/getProductConsultationList")
    GeneralResult<List<ProductConsultation>>  getProductConsultationList(String member_card);

    @ApiOperation("获取顾客病症")
    @GetMapping("/member/v1/getMemberDisease")
    GeneralResult<List<MemberDisease>> getMemberDisease(String member_card);

    @ApiOperation("新增收货地址")
    @GetMapping("/member/v1/addReveiverAddress")
    GeneralResult addReveiverAddress(@RequestBody ReveiverAddress reveiverAddress);


    @ApiOperation("修改收货地址")
    @GetMapping("/member/v1/updateReveiverAddress")
    GeneralResult updateReveiverAddress(@RequestBody ReveiverAddress reveiverAddress);

    @ApiOperation("获取收获地址")
    @GetMapping("/member/v1/getReveiverAddress")
    GeneralResult<List<ReveiverAddress>> getReveiverAddress(String member_card);

    @ApiOperation("获取购买能力")
    @GetMapping("/member/v1/getMemberOrderStat")
    GeneralResult<MemberOrderStat> getMemberOrderStat(String member_card);


    @ApiOperation("新增购买能力")
    @GetMapping("/member/v1/addMemberOrderStat")
    GeneralResult addMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

    @ApiOperation("修改购买能力")
    @GetMapping("/member/v1/updateMemberOrderStat")
    GeneralResult updateMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

    @ApiOperation("添加顾客行为偏好")
    @GetMapping("/member/v1/addMemberAction")
    GeneralResult addMemberAction(@RequestBody MemberAction memberAction);

    @ApiOperation("修改顾客行为偏好")
    @GetMapping("/member/v1/updateMemberAction")
    GeneralResult updateMemberAction(@RequestBody MemberAction memberAction);

    @ApiOperation("获取顾客行为偏好")
    @GetMapping("/member/v1/getMemberAction")
    GeneralResult<MemberAction> getMemberAction(String member_card);
}

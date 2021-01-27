package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;

import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.dto.member.CallInfoDTO;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.WorkOrderClient;
import cn.net.yzl.crm.service.micservice.member.MemberPhoneFien;
import cn.net.yzl.crm.sys.BizException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

@Api(tags = "顾客管理")
@Slf4j
@RestController
@RequestMapping(value = MemberController.PATH)
public class MemberController {
    public static final String PATH = "member";
    @Autowired
    MemberFien memberFien;
    @Autowired
    MemberPhoneFien memberPhoneFien;

    @Autowired
    WorkOrderClient workOrderClient;

    @ApiOperation(value = "分页查询顾客列表")
    @PostMapping("v1/listPage")
    public GeneralResult<Page<Member>> listPage( MemberSerchDTO dto) {
        GeneralResult<Page<Member>> result = memberFien.listPage(dto);
        return result;
    }

//    @ApiOperation(value = "顾客列表查询病症分类")
//    @GetMapping("v1/productClassi")
//    public GeneralResult<List<Map<Integer, Object>>> productClassi() {
//        List<Map<Integer, Object>> maps = memberService.productClassiService("0");
//        return GeneralResult.success(maps);
//    }

//    @ApiOperation(value = "顾客列表查询病症分类")
//    @GetMapping("v1/specific")
//    public GeneralResult<List<Map<Integer, Object>>> specific(@RequestParam("pid") String pid) {
//        List<Map<Integer, Object>> maps = memberService.productClassiService(pid);
//        return GeneralResult.success(maps);
//    }

    @ApiOperation(value = "保存顾客信息")
    @PostMapping("v1/save")
    public GeneralResult<Boolean> save(@RequestBody Member dto) {
        memberFien.save(dto);
        return GeneralResult.success();
    }

    @ApiOperation(value = "修改顾客信息")
    @PostMapping("v1/updateByMemberCard")
    public GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto) {
        GeneralResult<Boolean> result = memberFien.updateByMemberCard(dto);
        return result;
    }

    @ApiOperation(value = "获取顾客信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard) {
        return memberFien.getMember(memberCard);
    }

    @ApiOperation(value = "顾客列表-查询条件-获取顾客级别列表")
    @GetMapping("v1/getMemberGrad")
    public GeneralResult getMemberGrad() {
        GeneralResult<List<MemberGrad>> result = memberFien.getMemberGrad();
        return result;
    }
    @ApiOperation(value = "顾客列表-查询条件-根据媒体id获取广告列表")
    @GetMapping("v1/getAdverList")
    public GeneralResult getAdverList(String media_code) {
        return GeneralResult.success();
    }

    @ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
    @GetMapping("/v1/getMemberPhoneList")
    public GeneralResult getMemberPhoneList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<MemberPhone>> result = memberFien.getMemberPhoneList(member_card);
        return GeneralResult.success(result);
    }

    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     *
     * @param phone
     * @return
     */
    @ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
    //@GetMapping("/v1/getMemberByPhone")
    @GetMapping("/v1/getMemberByphoneNumber")
    public ComResponse getMemberByPhone(
            @RequestParam("phone")
            @NotBlank(message = "phone不能为空")
            @ApiParam(name = "phone", value = "手机号", required = true)
                    String phone) {
        ComResponse<Member> result = memberPhoneFien.getMemberByphoneNumber(phone);
        return result;
    }

    /**
     * 设置顾客为会员
     *
     * @param member_card
     * @return
     */
    @ApiOperation("设置顾客为会员")
    @GetMapping("/v1/setMemberToVip")
    public GeneralResult setMemberToVip(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        memberFien.setMemberToVip(member_card);
        return GeneralResult.success();
    }

    /**
     * 获取顾客购买商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客购买商品")
    @GetMapping("v1/getMemberProductEffectList")
    public GeneralResult getMemberProductEffectList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<MemberProductEffect>> result = memberFien.getMemberProductEffectList(member_card);
        return result;
    }

    /**
     * 获取顾客咨询商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客咨询商品")
    @GetMapping("v1/getProductConsultationList")
    public GeneralResult getProductConsultationList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<ProductConsultation>> result = memberFien.getProductConsultationList(member_card);
        return result;
    }

    /**
     * 获取顾客病症
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客病症")
    @GetMapping("v1/getMemberDisease")
    public GeneralResult getMemberDisease(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        GeneralResult<List<MemberDisease>> result = memberFien.getMemberDisease(member_card);
        return result;
    }



    @ApiOperation(value = "顾客收货地址-添加顾客收货地址", notes = "顾客收货地址-添加顾客收货地址")
    @RequestMapping(value = "/v1/addReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> addReveiverAddress(@RequestBody  @Validated ReveiverAddressInsertVO reveiverAddressInsertVO) throws IllegalAccessException {
        return memberFien.addReveiverAddress(reveiverAddressInsertVO);
    }

    @ApiOperation(value = "顾客收货地址-更新收货地址", notes = "顾客收货地址-更新收货地址")
    @RequestMapping(value = "/v1/updateReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> updateReveiverAddress(@RequestBody @Validated ReveiverAddressUpdateVO reveiverAddressUpdateVO) throws IllegalAccessException {
        return memberFien.updateReveiverAddress(reveiverAddressUpdateVO);
    }

    @ApiOperation(value = "顾客收货地址-获取顾客收货地址", notes = "顾客收货地址-获取顾客收货地址")
    @RequestMapping(value = "/v1/getReveiverAddress", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard) {
        return memberFien.getReveiverAddress(memberCard);
    }


    @ApiOperation("保存顾客购买能力")
    @PostMapping("/v1/saveMemberOrderStat")
    public GeneralResult saveMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat) {
        if (memberOrderStat == null) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        if (memberOrderStat.getId() == 0) {
            memberFien.addMemberOrderStat(memberOrderStat);
        } else {
            memberFien.updateMemberOrderStat(memberOrderStat);
        }
        return GeneralResult.success();
    }

    @ApiOperation("获取顾客购买能力")
    @GetMapping("/v1/getMemberOrderStat")
    public GeneralResult getMemberOrderStat(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        GeneralResult<MemberOrderStat> result = memberFien.getMemberOrderStat(member_card);
        return result;
    }

//    @ApiOperation("保存顾客行为偏好")
//    @PostMapping("/v1/saveMemberAction")
//    public GeneralResult saveMemberAction(@RequestBody MemberAction memberAction) {
//        if (memberAction == null || StringUtil.isNullOrEmpty(memberAction.getMember_card()))
//            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
//        GeneralResult<MemberAction> result = memberFien.getMemberAction(memberAction.getMember_card());
//        if (result.getData() == null) {
//            memberFien.addMemberAction(memberAction);
//        } else {
//            memberFien.updateMemberAction(memberAction);
//        }
//        return GeneralResult.success();
//    }
//
//    @ApiOperation("获取顾客行为偏好")
//    @GetMapping("/v1/getMemberAction")
//    public GeneralResult getMemberAction(
//            @RequestParam("member_card")
//            @NotBlank(message = "member_card不能为空")
//            @ApiParam(name = "member_card", value = "会员卡号", required = true)
//                    String member_card
//    ) {
//        GeneralResult<MemberAction> result = memberFien.getMemberAction(member_card);
//        return result;
//    }

    @ApiOperation("获取沟通记录")
    @PostMapping("/v1/getCallRecard")
    public ComResponse<Page<StaffCallRecord>> getCallRecard(@RequestBody CallInfoDTO callInfoDTO) {
        if (callInfoDTO == null || callInfoDTO.getPageSize() == 0 || callInfoDTO.getPageNo() == 0) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse<Page<StaffCallRecord>> response = workOrderClient.getCallRecord(callInfoDTO);
        return response;
    }

    @ApiOperation("获取顾客行为偏好字典数据")
    @GetMapping("/v1/getMemberActions")
    public ComResponse getMemberActions() {
        return ComResponse.success(memberFien.getMemberActions());
    }



    @ApiOperation(value = "顾客账户-获取顾客账户信息", notes = "顾客账户-获取顾客账户信息")
    @RequestMapping(value = "/getMemberAmount", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
    })
    ComResponse<MemberAmountDto> getMemberAmount(@RequestParam("memberCard") String  memberCard) {
        return memberFien.getMemberAmount(memberCard);
    }
    @ApiOperation(value = "顾客账户-获取余额明细", notes = "顾客账户-获取余额明细")
    @RequestMapping(value = "/getMemberAmountDetailList", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "timeFlag", value = "时间标识(1:最近三个月,2:三个月以前的)", required = true, dataType = "Int", paramType = "query"),
    })
    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(@RequestParam("memberCard") String  memberCard, @RequestParam("timeFlag") Integer timeFlag) throws ParseException {
        return memberFien.getMemberAmountDetailList(memberCard,timeFlag);
    }
}

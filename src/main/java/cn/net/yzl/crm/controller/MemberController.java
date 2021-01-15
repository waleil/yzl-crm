package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.dto.member.CallInfoDTO;
import cn.net.yzl.crm.dto.order.ListParamsDTO;
import cn.net.yzl.crm.dto.staff.CallnfoCriteriaTO;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import cn.net.yzl.crm.model.Media;
//import cn.net.yzl.crm.model.Member;
//import cn.net.yzl.crm.model.MemberGrade;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.MemberService;
import cn.net.yzl.crm.service.micservice.CoopCompanyMediaFien;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.WorkOrderClient;
import cn.net.yzl.crm.sys.BizException;
import com.github.pagehelper.PageInfo;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "顾客管理")
@Slf4j
@RestController
@RequestMapping(value = MemberController.PATH)
public class MemberController {
    public static final String PATH = "member";
//    @Autowired
//    private MemberService memberService;

    @Autowired
    MemberFien memberFien;
    @Autowired
    CoopCompanyMediaFien coopCompanyMediaFien;

    @Autowired
    WorkOrderClient workOrderClient;

    @ApiOperation(value = "分页查询顾客列表")
    @PostMapping("v1/listPage")
    public GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto) {
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
    @PostMapping("v1/updateByMemberCart")
    public GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto) {
        memberFien.updateByMemberCard(dto);
        return GeneralResult.success();
    }

    @ApiOperation(value = "获取顾客信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard) {
        return memberFien.getMember(memberCard);
    }

    @ApiOperation(value = "获取顾客级别")
    @GetMapping("v1/getMemberGrad")
    public GeneralResult getMemberGrad() {
        GeneralResult<List<MemberGrad>> result = memberFien.getMemberGrad();
        return result;
    }

    @ApiOperation(value = "获取媒体列表")
    @GetMapping("v1/getMediaList")
    public GeneralResult<List<Media>> getMediaList() {
        GeneralResult<List<Media>> result = coopCompanyMediaFien.getMediaList();
        return result;
    }

    @ApiOperation(value = "根据媒体id获取广告列表列表")
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
    @GetMapping("/v1/getMemberByPhone")
    public GeneralResult getMemberByPhone(
            @RequestParam("phone")
            @NotBlank(message = "phone不能为空")
            @ApiParam(name = "phone", value = "手机号", required = true)
                    String phone) {
        GeneralResult<Member> result = memberFien.getMemberByPhone(phone);
        if (result == null) return GeneralResult.errorWithMessage(101, "远程服务器无响应");
        return GeneralResult.success(result);
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

    @ApiOperation("保存收货地址")
    @PostMapping("/v1/saveReveiverAddress")
    public GeneralResult saveReveiverAddress(@RequestBody ReveiverAddress reveiverAddress) {
        if (reveiverAddress == null) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        if (StringUtil.isNullOrEmpty(reveiverAddress.getMember_card())) {
            // todo 获取code码
            memberFien.addReveiverAddress(reveiverAddress);
        } else {
            memberFien.updateReveiverAddress(reveiverAddress);
        }

        return GeneralResult.success();
    }

    @ApiOperation("获取收货地址")
    @GetMapping("/v1/getReveiverAddress")
    public GeneralResult getReveiverAddress(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        GeneralResult<List<ReveiverAddress>> result = memberFien.getReveiverAddress(member_card);
        return result;
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

    @ApiOperation("保存顾客行为偏好")
    @PostMapping("/v1/saveMemberAction")
    public GeneralResult saveMemberAction(@RequestBody MemberAction memberAction) {
        if (memberAction == null || StringUtil.isNullOrEmpty(memberAction.getMember_card()))
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        GeneralResult<MemberAction> result = memberFien.getMemberAction(memberAction.getMember_card());
        if (result.getData() == null) {
            memberFien.addMemberAction(memberAction);
        } else {
            memberFien.updateMemberAction(memberAction);
        }
        return GeneralResult.success();
    }

    @ApiOperation("获取顾客行为偏好")
    @GetMapping("/v1/getMemberAction")
    public GeneralResult getMemberAction(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        GeneralResult<MemberAction> result = memberFien.getMemberAction(member_card);
        return result;
    }

    @ApiOperation("获取沟通记录")
    @PostMapping("/v1/getCallRecard")
    public ComResponse<Page<StaffCallRecord>> getCallRecard(@RequestBody CallInfoDTO callInfoDTO) {
        if (callInfoDTO == null || callInfoDTO.getPageSize() == 0 || callInfoDTO.getPageNo() == 0) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse<Page<StaffCallRecord>> response = workOrderClient.getCallRecord(callInfoDTO);
        return response;
    }

    @ApiOperation("保存顾客圈选")
    @PostMapping("/v1/saveMemberCrowdGroup")
    public ComResponse saveMemberCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup) {
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        if (StringUtil.isNullOrEmpty(memberCrowdGroup.getCrowd_id())) {
            String crowd_id = UUID.randomUUID().toString().replaceAll("-", "");
            memberCrowdGroup.setCrowd_id(crowd_id);
            return memberFien.addCrowdGroup(memberCrowdGroup);
        } else {
            return memberFien.updateCrowdGroup(memberCrowdGroup);
        }
    }

    @ApiOperation("根据圈选id获取圈选信息")
    @GetMapping("/v1/getMemberCrowdGroup")
    public ComResponse getMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId
    ) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        ComResponse<member_crowd_group> result = memberFien.getMemberCrowdGroup(crowdId);
        if (result.getData() == null || result.getData().isDel())
            return ComResponse.success(ResponseCodeEnums.NO_DATA_CODE);
        return result;
    }

    @ApiOperation("获取顾客行为偏好字典数据")
    @GetMapping("/v1/getMemberActions")
    public ComResponse getMemberActions() {
        return ComResponse.success(memberFien.getMemberActions());
    }

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    public ComResponse delMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId
    ) {
        return memberFien.delMemberCrowdGroup(crowdId);
    }

    @ApiOperation("根据一批顾客群组id获取群组信息,用英文逗号分隔")
    @GetMapping("/v1/getCrowdGroupList")
    public ComResponse getCrowdGroupList(
            @RequestParam("crowdIds")
            @NotBlank(message = "crowdIds不能为空")
            @ApiParam(name = "crowdIds", value = "圈选id", required = true)
                    String crowdIds
    ) {
        if (StringUtil.isNullOrEmpty(crowdIds)) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        return memberFien.getCrowdGroupList(crowdIds);
    }
}

package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberGrad;
import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.dto.order.ListParamsDTO;
import cn.net.yzl.crm.model.Media;
//import cn.net.yzl.crm.model.Member;
//import cn.net.yzl.crm.model.MemberGrade;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.MemberService;
import cn.net.yzl.crm.service.micservice.CoopCompanyMediaFien;
import cn.net.yzl.crm.service.micservice.MemberFien;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "顾客管理")
@Slf4j
@RestController
@RequestMapping(value = MemberController.PATH)
public class MemberController {
    public static final String PATH = "member";
    @Autowired
    private MemberService memberService;

    @Autowired
    MemberFien memberFien;
    @Autowired
    CoopCompanyMediaFien coopCompanyMediaFien;

    @ApiOperation(value = "分页查询顾客列表")
    @PostMapping("v1/listPage")
    public GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto) {
        GeneralResult<Page<Member>> result =  memberFien.listPage(dto);
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
        GeneralResult<List<MemberGrad>> result=  memberFien.getMemberGrad();
        return result;
    }

    @ApiOperation(value = "获取媒体列表")
    @GetMapping("v1/getMediaList")
    public GeneralResult<List<Media>> getMediaList() {
        GeneralResult<List<Media>> result= coopCompanyMediaFien.getMediaList();
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
}

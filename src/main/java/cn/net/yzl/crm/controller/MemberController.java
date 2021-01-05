package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.dto.order.ListParamsDTO;
import cn.net.yzl.crm.model.Media;
import cn.net.yzl.crm.model.Member;
import cn.net.yzl.crm.model.MemberGrade;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.MemberService;
import cn.net.yzl.crm.service.micservice.CoopCompanyMediaFien;
import cn.net.yzl.crm.service.micservice.MemberFien;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public GeneralResult<Boolean> updateByMemberCart(@RequestBody Member dto) {
        memberFien.updateByMemberCart(dto);
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
        GeneralResult<List<MemberGrade>> result=  memberFien.getMemberGrad();
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
}

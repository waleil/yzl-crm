package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.model.*;

import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 顾客服务接口
 */
@FeignClient(name = "crmCustomer",url = "${api.gateway.url}/crmCustomer/member")
//@FeignClient(value = "yzl-crm-customer-api")
public interface MemberFien {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/getMemberListByPage")
    GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto);

    @ApiOperation("保存会员信息")
    @PostMapping("/v1/save")
    GeneralResult<Boolean> save(@RequestBody Member dto);

    @ApiOperation("修改会员信息")
    @PostMapping("/v1/updateByMemberCard")
    GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto);

    @ApiOperation("根据卡号获取会员信息")
    @GetMapping("/v1/getMember")
    GeneralResult<Member> getMember(@RequestParam("memberCard") String  memberCard);

    @ApiOperation("获取会员等级")
    @GetMapping("/v1/getMemberGrad")
    GeneralResult<List<MemberGrad>> getMemberGrad();

    @ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
    @GetMapping("/v1/getMemberPhoneList")
    GeneralResult<List<MemberPhone>> getMemberPhoneList(@RequestParam("member_card") String member_card);

    @ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
    @GetMapping("/v1/getMemberByPhone")
    GeneralResult<Member> getMemberByPhone(@RequestParam("phone") String phone);

    @ApiOperation("设置顾客为会员")
    @GetMapping("/v1/setMemberToVip")
    void   setMemberToVip(@RequestParam("member_card") String member_card);

    @ApiOperation("获取顾客购买商品")
    @GetMapping("/v1/getMemberProductEffectList")
    GeneralResult<List<MemberProductEffect>> getMemberProductEffectList(@RequestParam("member_card") String member_card);

    @ApiOperation("获取顾客咨询商品")
    @GetMapping("/v1/getProductConsultationList")
    GeneralResult<List<ProductConsultation>>  getProductConsultationList(@RequestParam("member_card") String member_card);

    @ApiOperation("获取顾客病症")
    @GetMapping("/v1/getMemberDisease")
    GeneralResult<List<MemberDisease>> getMemberDisease(@RequestParam("member_card") String member_card);

    @ApiOperation("新增收货地址")
    @GetMapping("/v1/addReveiverAddress")
    GeneralResult addReveiverAddress(@RequestBody ReveiverAddress reveiverAddress);


    @ApiOperation("修改收货地址")
    @GetMapping("/v1/updateReveiverAddress")
    GeneralResult updateReveiverAddress(@RequestBody ReveiverAddress reveiverAddress);

    @ApiOperation("获取收获地址")
    @GetMapping("/v1/getReveiverAddress")
    GeneralResult<List<ReveiverAddress>> getReveiverAddress(@RequestParam("member_card") String member_card);

    @ApiOperation("获取购买能力")
    @GetMapping("/v1/getMemberOrderStat")
    GeneralResult<MemberOrderStat> getMemberOrderStat(@RequestParam("member_card") String member_card);


    @ApiOperation("新增购买能力")
    @GetMapping("/v1/addMemberOrderStat")
    GeneralResult addMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

    @ApiOperation("修改购买能力")
    @GetMapping("/v1/updateMemberOrderStat")
    GeneralResult updateMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

//    @ApiOperation("添加顾客行为偏好")
//    @GetMapping("/v1/addMemberAction")
//    GeneralResult addMemberAction(@RequestBody MemberAction memberAction);
//
//    @ApiOperation("修改顾客行为偏好")
//    @GetMapping("/v1/updateMemberAction")
//    GeneralResult updateMemberAction(@RequestBody MemberAction memberAction);
//
//    @ApiOperation("获取顾客行为偏好")
//    @GetMapping("/v1/getMemberAction")
//    GeneralResult<MemberAction> getMemberAction(@RequestParam("member_card") String member_card);

    @ApiOperation("新增顾客圈选")
    @PostMapping("/v1/addCrowdGroup")
    ComResponse  addCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup);

    @ApiOperation("修改顾客圈选")
    @PostMapping("/v1/updateCrowdGroup")
    ComResponse updateCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup);

    @ApiOperation("根据圈选id获取圈选信息")
    @GetMapping("/v1/getMemberCrowdGroup")
    ComResponse getMemberCrowdGroup(@RequestParam("crowdId") String crowdId);

    @ApiOperation("根据一批顾客群组id获取群组信息,用英文逗号分隔")
    @GetMapping("/v1/getCrowdGroupList")
    ComResponse<List<CrowdGroup>> getCrowdGroupList( @RequestParam("crowdGroupIds")String crowdGroupIds);

    @ApiOperation("分页获取顾客圈选列表")
    @PostMapping("/v1/getCrowdGroupByPage")
    ComResponse getCrowdGroupByPage(@RequestBody CrowdGroupDTO crowdGroupDTO);

    @ApiOperation("获取顾客行为偏好字典数据")
    @GetMapping("/v1/getMemberActions")
    ComResponse<List<MemberBaseAttr>> getMemberActions();

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    ComResponse delMemberCrowdGroup(@RequestParam("crowdId") String crowdId);
}

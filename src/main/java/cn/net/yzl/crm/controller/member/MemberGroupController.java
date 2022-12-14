package cn.net.yzl.crm.controller.member;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.label.MemberLabelDto;
import cn.net.yzl.crm.customer.mongomodel.crowd.MemberCrowdGroupOpVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.dto.member.MemberCrowdGroupDTO;
import cn.net.yzl.crm.model.customer.UpdateCrowdStatus;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberGroupController
 * @description 顾客圈选接口
 * @date: 2021/1/22 3:07 下午
 */
@Api(tags = "顾客圈选")
@RestController(value = "memberGroup")
public class MemberGroupController {
    @Autowired
    private MemberGroupFeign memberGroupFeign;

    @ApiOperation("保存顾客圈选")
    @PostMapping("/v1/saveMemberCrowdGroup")
    public ComResponse saveMemberCrowdGroup(@RequestBody MemberCrowdGroupDTO memberCrowdGroup) {
        if (memberCrowdGroup == null)
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        //获取操作人id
        String userId= QueryIds.userNo.get();
        member_crowd_group member_group = new member_crowd_group();
        member_group.setAge(memberCrowdGroup.getAge());
        member_group.setEmail(memberCrowdGroup.getEmail());
        member_group.setFirst_order_am(memberCrowdGroup.getFirst_order_am());
        member_group.setNature(memberCrowdGroup.getNature());
        member_group.setProducts(memberCrowdGroup.getProducts());
        member_group.setQq(memberCrowdGroup.getQq());
        member_group.setSex(memberCrowdGroup.getSex());
        member_group.setTotal_amount(memberCrowdGroup.getTotal_amount());
        member_group.setWechat(memberCrowdGroup.getWechat());
        member_group.setActions(memberCrowdGroup.getActions());
        member_group.setCrowd_name(memberCrowdGroup.getCrowd_name());
        member_group.setActive_degree(memberCrowdGroup.getActive_degree());
        member_group.setActive_order(memberCrowdGroup.getActive_order());
        member_group.setActive_like(memberCrowdGroup.getActive_like());
        member_group.setAdvers(memberCrowdGroup.getAdvers());
        member_group.setAreas(memberCrowdGroup.getAreas());
        member_group.setDel(false);
        member_group.setDescription(memberCrowdGroup.getDescription());
        member_group.setDiseases(memberCrowdGroup.getDiseases());
        member_group.setFirst_order_to_days(memberCrowdGroup.getFirst_order_to_days());
        member_group.setHave_order(memberCrowdGroup.getHave_order());
        member_group.setTotal_amount(memberCrowdGroup.getTotal_amount());
        member_group.setTicket(memberCrowdGroup.getTicket());
        member_group.setLast_order_to_days(memberCrowdGroup.getLast_order_to_days());
        member_group.setLogistics_company_id(memberCrowdGroup.getLogistics_company_id());
        member_group.setLogistics_state(memberCrowdGroup.getLogistics_state());
        member_group.setMediaList(memberCrowdGroup.getMediaList());
        member_group.setIntegral(memberCrowdGroup.getIntegral());
        member_group.setOrder_action(memberCrowdGroup.getOrder_action());
        member_group.setMember_grade(memberCrowdGroup.getMember_grade());
        member_group.setMember_month(memberCrowdGroup.getMember_month());
        member_group.setMember_type(memberCrowdGroup.getMember_type());
        member_group.setOrder_high_am(memberCrowdGroup.getOrder_high_am());
        member_group.setOrder_low_am(memberCrowdGroup.getOrder_low_am());
        member_group.setPay_form(memberCrowdGroup.getPay_form());
        member_group.setPay_state(memberCrowdGroup.getPay_state());
        member_group.setPay_type(memberCrowdGroup.getPay_type());
        member_group.setPhone_time(memberCrowdGroup.getPhone_time());
        member_group.setRecharge(memberCrowdGroup.getRecharge());
        member_group.setRed_bag(memberCrowdGroup.getRed_bag());
        member_group.setPerson_count(memberCrowdGroup.getPerson_count());
        member_group.setResponse_time(memberCrowdGroup.getResponse_time());
        member_group.setOrder_rec_amount(memberCrowdGroup.getOrder_rec_amount());
        member_group.setOrder_source(memberCrowdGroup.getOrder_source());
        member_group.setOrder_state(memberCrowdGroup.getOrder_state());
        member_group.setSign_date_to_days(memberCrowdGroup.getSign_date_to_days());
        member_group.setStaff_sex(memberCrowdGroup.getStaff_sex());
        member_group.setVip(memberCrowdGroup.getVip());
        member_group.setActiveCodeList(memberCrowdGroup.getActiveCodeList());
        member_group.setActiveTypeList(memberCrowdGroup.getActiveTypeList());
        member_group.setOrder_total_amount(memberCrowdGroup.getOrder_total_amount());
        member_group.setUpdate_time(DateHelper.getCurrentDate());
        //默认是启用状态
        member_group.setEnable(1);
        member_group.setCreate_name(memberCrowdGroup.getCreate_name());
        member_group.setAdvertProducts(memberCrowdGroup.getAdvertProducts());
        member_group.setLastCallDays(memberCrowdGroup.getLastCallDays());
        //设置优先级
        member_group.setSeq(memberCrowdGroup.getSeq());
        if (memberCrowdGroup.getSeq() == null) {
            member_group.setSeq(4);
        }
        if (StringUtil.isNullOrEmpty(memberCrowdGroup.get_id())) {
            member_group.setCreate_code(userId);
            ComResponse result= memberGroupFeign.addCrowdGroup(member_group);
            return result;
        } else {
            //设置vo的id
            member_group.set_id(memberCrowdGroup.get_id());
            member_group.setUpdate_code(userId);
            ComResponse result= memberGroupFeign.updateCrowdGroup(member_group);
            return result;
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
        ComResponse<member_crowd_group> result = memberGroupFeign.getMemberCrowdGroup(crowdId);
        if (result.getData() == null )
            return ComResponse.success(ResponseCodeEnums.NO_DATA_CODE);
        return result;
    }
    @ApiOperation("圈选试算")
    @PostMapping("/v1/groupTrial")
    public ComResponse<Integer> memberCrowdGroupTrial(@RequestBody MemberCrowdGroupDTO memberCrowdGroup){
        return memberGroupFeign.memberCrowdGroupTrial(buildMemberCrowdGroup(memberCrowdGroup));
    }
    @ApiOperation("圈选试算同时返回部分客户数据")
    @PostMapping("/v1/groupTrialPullData")
    public ComResponse<Page<MemberLabelDto>> memberCrowdGroupTrialPullData(@RequestBody MemberCrowdGroupDTO memberCrowdGroup){
        return memberGroupFeign.memberCrowdGroupTrialPullData(buildMemberCrowdGroup(memberCrowdGroup));
    }

    @ApiOperation("通过群组Id圈选试算")
    @PostMapping("/v1/groupTrialById")
    public ComResponse<Integer> memberCrowdGroupTrialById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO) {
        return memberGroupFeign.memberCrowdGroupTrialById(crowdGroupOpVO);
    }

    @ApiOperation("通过群组Id圈选试算同时返回部分客户数据")
    @PostMapping("/v1/groupTrialPullDataById")
    public ComResponse<Page<MemberLabelDto>> memberCrowdGroupTrialPullDataById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO){
        return memberGroupFeign.memberCrowdGroupTrialPullDataById(crowdGroupOpVO);
    }

    @ApiOperation("通过群组Id圈选")
    @PostMapping("/v1/groupRunById")
    public ComResponse<Integer> memberCrowdGroupRunById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO){
        return memberGroupFeign.memberCrowdGroupRunById(crowdGroupOpVO);
    }

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    public ComResponse delMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId
    ) {
        return memberGroupFeign.delMemberCrowdGroup(crowdId);
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
        return memberGroupFeign.getCrowdGroupList(crowdIds);
    }

    @ApiOperation("分页获取圈选列表")
    @GetMapping("/v1/getCrowdGroupByPage")
    public ComResponse getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {
        if (crowdGroupDTO == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        return memberGroupFeign.getCrowdGroupByPage(crowdGroupDTO);
    }
    @ApiOperation("修改圈选规则状态")
    @PostMapping("/v1/updateStatus")
    public ComResponse updateMemberCrowdGroupStatus(@RequestBody @Valid UpdateCrowdStatus updateCrowdStatus){
        UpdateCrowdStatusVO vo = new UpdateCrowdStatusVO();
        vo.set_id(updateCrowdStatus.get_id());
        vo.setEnable(updateCrowdStatus.getEnable());
        //获取操作人id
        String userId= QueryIds.userNo.get();
        vo.setUpdate_code(userId);
        return memberGroupFeign.updateCustomerCrowdGroupStatus(vo);
    }
    /**
     * @Author: lichanghong
     * @Description:  查询圈选规则
     * @Date: 2021/1/22 8:20 下午
     * @param
     * @Return: cn.net.yzl.common.entity.ComResponse<cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO>
     */
    @ApiOperation("查询顾客圈选列表")
    @GetMapping("/v1/query4Select")
    public ComResponse<List<CustomerCrowdGroupVO>> query4Select(){
        return memberGroupFeign.query4Select();
    }
    /**
     * @Author: lichanghong
     * @Description: 根据分组编号查询关联的顾客
     * @Date: 2021/1/28 12:54 上午
     * @param groupId
     * @Return: java.util.List<cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember>
     */
    @ApiOperation("根据圈选编号查询顾客")
    @GetMapping("/v1/queryMemberByGroupId")
    public ComResponse<List<GroupRefMember>> queryMembersByGroupId(@RequestParam String groupId){
        return memberGroupFeign.queryMembersByGroupId(groupId);
    }


    public member_crowd_group buildMemberCrowdGroup(MemberCrowdGroupDTO memberCrowdGroup){
        member_crowd_group member_group = new member_crowd_group();
        member_group.setAge(memberCrowdGroup.getAge());
        member_group.setEmail(memberCrowdGroup.getEmail());
        member_group.setFirst_order_am(memberCrowdGroup.getFirst_order_am());
        member_group.setNature(memberCrowdGroup.getNature());
        member_group.setProducts(memberCrowdGroup.getProducts());
        member_group.setQq(memberCrowdGroup.getQq());
        member_group.setSex(memberCrowdGroup.getSex());
        member_group.setTotal_amount(memberCrowdGroup.getTotal_amount());
        member_group.setWechat(memberCrowdGroup.getWechat());
        member_group.setActions(memberCrowdGroup.getActions());
        member_group.setCrowd_name(memberCrowdGroup.getCrowd_name());
        member_group.setActive_degree(memberCrowdGroup.getActive_degree());
        member_group.setActive_order(memberCrowdGroup.getActive_order());
        member_group.setActive_like(memberCrowdGroup.getActive_like());
        member_group.setAdvers(memberCrowdGroup.getAdvers());
        member_group.setAreas(memberCrowdGroup.getAreas());
        member_group.setDescription(memberCrowdGroup.getDescription());
        member_group.setDiseases(memberCrowdGroup.getDiseases());
        member_group.setFirst_order_to_days(memberCrowdGroup.getFirst_order_to_days());
        member_group.setHave_order(memberCrowdGroup.getHave_order());
        member_group.setTotal_amount(memberCrowdGroup.getTotal_amount());
        member_group.setTicket(memberCrowdGroup.getTicket());
        member_group.setLast_order_to_days(memberCrowdGroup.getLast_order_to_days());
        member_group.setLogistics_company_id(memberCrowdGroup.getLogistics_company_id());
        member_group.setLogistics_state(memberCrowdGroup.getLogistics_state());
        member_group.setMediaList(memberCrowdGroup.getMediaList());
        member_group.setIntegral(memberCrowdGroup.getIntegral());
        member_group.setOrder_action(memberCrowdGroup.getOrder_action());
        member_group.setMember_grade(memberCrowdGroup.getMember_grade());
        member_group.setMember_month(memberCrowdGroup.getMember_month());
        member_group.setMember_type(memberCrowdGroup.getMember_type());
        member_group.setOrder_high_am(memberCrowdGroup.getOrder_high_am());
        member_group.setOrder_low_am(memberCrowdGroup.getOrder_low_am());
        member_group.setPay_form(memberCrowdGroup.getPay_form());
        member_group.setPay_state(memberCrowdGroup.getPay_state());
        member_group.setPay_type(memberCrowdGroup.getPay_type());
        member_group.setPhone_time(memberCrowdGroup.getPhone_time());
        member_group.setRecharge(memberCrowdGroup.getRecharge());
        member_group.setRed_bag(memberCrowdGroup.getRed_bag());
        member_group.setResponse_time(memberCrowdGroup.getResponse_time());
        member_group.setOrder_rec_amount(memberCrowdGroup.getOrder_rec_amount());
        member_group.setOrder_source(memberCrowdGroup.getOrder_source());
        member_group.setOrder_state(memberCrowdGroup.getOrder_state());
        member_group.setSign_date_to_days(memberCrowdGroup.getSign_date_to_days());
        member_group.setStaff_sex(memberCrowdGroup.getStaff_sex());
        member_group.setVip(memberCrowdGroup.getVip());
        member_group.setActiveCodeList(memberCrowdGroup.getActiveCodeList());
        member_group.setActiveTypeList(memberCrowdGroup.getActiveTypeList());
        member_group.setOrder_total_amount(memberCrowdGroup.getOrder_total_amount());
        member_group.setAdvertProducts(memberCrowdGroup.getAdvertProducts());
        member_group.setLastCallDays(memberCrowdGroup.getLastCallDays());
        return member_group;
    }
}

package cn.net.yzl.crm.controller.member;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.dto.member.MemberCrowdGroupDTO;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

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
        if (memberCrowdGroup == null ||memberCrowdGroup.getEffective_date()==null
                || memberCrowdGroup.getExpire_date()==null)
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        if(memberCrowdGroup.getExpire_date().before(memberCrowdGroup.getEffective_date())){
            throw  new BizException(101,"日期选择错误");
        }
        if(memberCrowdGroup.getExpire_date().before(DateHelper.getCurrentDate())){
            throw  new BizException(101,"失效日期在当前日期之前");
        }
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
        member_group.setCrowd_id(memberCrowdGroup.getCrowd_id());
        member_group.setCrowd_name(memberCrowdGroup.getCrowd_name());
        member_group.setActive_degree(memberCrowdGroup.getActive_degree());
        member_group.setActive_order(member_group.getActive_order());
        member_group.setActive_like(memberCrowdGroup.getActive_like());
        member_group.setAdvers(memberCrowdGroup.getAdvers());
        member_group.setAreas(memberCrowdGroup.getAreas());
        member_group.setDel(false);
        member_group.setDescription(memberCrowdGroup.getDescription());
        member_group.setDiseases(memberCrowdGroup.getDiseases());
        member_group.setEffective_date(memberCrowdGroup.getEffective_date());
        member_group.setExpire_date(memberCrowdGroup.getExpire_date());
        member_group.setFirst_order_to_days(memberCrowdGroup.getFirst_order_to_days());
        member_group.setHave_order(memberCrowdGroup.getHave_order());
        member_group.setTotal_amount(memberCrowdGroup.getTotal_amount());
        member_group.setTicket(memberCrowdGroup.getTicket());
        member_group.setLast_order_to_days(memberCrowdGroup.getLast_order_to_days());
        member_group.setLogistics_company_id(memberCrowdGroup.getLogistics_company());
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
        //  member_group.setPerson_count(memberCrowdGroup.); //人数
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
        member_group.setActiveList(memberCrowdGroup.getActiveList());
        member_group.setOrder_total_amount(memberCrowdGroup.getOrder_total_amount());
        member_group.setUpdate_time(DateHelper.getCurrentDate());
        member_group.setEnable(0);
        Date currentDate=DateHelper.getCurrentDate();

        if(currentDate.after(memberCrowdGroup.getEffective_date()) && currentDate.before(memberCrowdGroup.getExpire_date())){
            member_group.setEnable(1);
        }

        //todo增加创建人， 修改人信息

        if (StringUtil.isNullOrEmpty(memberCrowdGroup.getCrowd_id())) {
            String crowd_id = UUID.randomUUID().toString().replaceAll("-", "");

            member_group.setCrowd_id(crowd_id);
            member_group.setCreate_time(DateHelper.getCurrentDate());

            ComResponse result= memberGroupFeign.addCrowdGroup(member_group);
            return result;
        } else {

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
}

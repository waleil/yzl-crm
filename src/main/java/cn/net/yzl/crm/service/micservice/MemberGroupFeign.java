package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.label.MemberLabelDto;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.MemberCrowdGroupOpVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberGroupFegin
 * @date: 2021/1/22 3:09 下午
 */
@FeignClient(name = "crmCustomerGroup",url = "${api.gateway.url}/crmCustomer/customerGroup")
//@FeignClient(name = "crmCustomerGroup",url = "http://127.0.0.1:2070/customerGroup")
public interface MemberGroupFeign {
    @ApiOperation("新增顾客圈选")
    @PostMapping("/v1/addCrowdGroup")
    ComResponse addCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup);

    @ApiOperation("修改顾客圈选")
    @PostMapping("/v1/updateCrowdGroup")
    ComResponse updateCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup);

    @ApiOperation("根据圈选id获取圈选信息")
    @GetMapping("/v1/getMemberCrowdGroup")
    ComResponse getMemberCrowdGroup(@RequestParam("crowdId") String crowdId);

    @ApiOperation("根据一批顾客群组id获取群组信息,用英文逗号分隔")
    @GetMapping("/v1/getCrowdGroupList")
    ComResponse<List<CrowdGroup>> getCrowdGroupList(@RequestParam("crowdGroupIds")String crowdGroupIds);

    @ApiOperation("分页获取顾客圈选列表")
    @GetMapping("/v1/getCrowdGroupByPage")
    ComResponse<Page<member_crowd_group>> getCrowdGroupByPage(@SpringQueryMap CrowdGroupDTO crowdGroupDTO);

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    ComResponse delMemberCrowdGroup(@RequestParam("crowdId") String crowdId);
    /**
     * @Author: lichanghong
     * @Description: 修改圈选规则状态
     * @Date: 2021/1/22 8:39 下午
     * @Return:
     */
    @PostMapping("/v1/updateStatus")
    ComResponse updateCustomerCrowdGroupStatus(@RequestBody UpdateCrowdStatusVO vo);
    /**
     * @Author: lichanghong
     * @Description:  查询圈选规则
     * @Date: 2021/1/22 8:20 下午
     * @param
     * @Return: cn.net.yzl.common.entity.ComResponse<cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO>
     */
    @GetMapping("/v1/query4Select")
    ComResponse<List<CustomerCrowdGroupVO>> query4Select();

    @ApiOperation("圈选试算")
    @PostMapping("/v1/groupTrial")
    ComResponse<Integer> memberCrowdGroupTrial(@RequestBody member_crowd_group memberCrowdGroup);

    @ApiOperation("圈选试算同时返回部分客户数据")
    @PostMapping("/v1/groupTrialPullData")
    ComResponse<Page<MemberLabelDto>> memberCrowdGroupTrialPullData(@RequestBody member_crowd_group memberCrowdGroup);

    @GetMapping("/v1/queryMemberByGroupId")
    ComResponse<List<GroupRefMember>> queryMembersByGroupId(@RequestParam("groupId") String groupId);

    @ApiOperation("通过群组Id圈选试算")
    @PostMapping("/v1/groupTrialById")
    ComResponse<Integer> memberCrowdGroupTrialById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO);

    @ApiOperation("通过群组Id圈选试算同时返回部分客户数据")
    @PostMapping("/v1/groupTrialPullDataById")
    ComResponse<Page<MemberLabelDto>> memberCrowdGroupTrialPullDataById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO);

    @ApiOperation("通过群组Id圈选")
    @PostMapping("/v1/groupRunById")
    ComResponse<Integer> memberCrowdGroupRunById(@RequestBody MemberCrowdGroupOpVO crowdGroupOpVO);




}

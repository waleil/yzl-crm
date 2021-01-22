package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.customer.model.MemberBaseAttr;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
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
    ComResponse getCrowdGroupByPage(@RequestBody CrowdGroupDTO crowdGroupDTO);

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    ComResponse delMemberCrowdGroup(@RequestParam("crowdId") String crowdId);
}

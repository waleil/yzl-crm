package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.dto.member.ActionDictDto;
import cn.net.yzl.crm.model.customer.ActionDict;
import cn.net.yzl.crm.model.customer.MemberActionRelation;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 顾客综合行为服务
 */
@FeignClient(name = "memberActionFeign", url = "${api.gateway.url}/crmCustomer/member/memberAction")
//@FeignClient(name = "crmCustomer", url = "localhost:2070/member/memberAction")
public interface MemberActionFeign {

    @GetMapping("v1/getActionDictList")
    public ComResponse<List<ActionDict>> getListByType(@RequestParam("type")  Integer type);

    @PostMapping("v1/saveUpdateActionDict")
    public ComResponse<Integer> saveUpdateActionDict(@RequestBody List<ActionDictDto> ageDictDtos);

    @GetMapping("v1/getRelationByMemberCard")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCard(@RequestParam("cardNo") String cardNo);

    @GetMapping("v1/getRelationByMemberCardAndType")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCardAndType(@RequestParam("cardNo") String cardNo,@RequestParam("type") Integer type);

    @PostMapping("v1/memberAgeRelationSaveUpdate")
    public ComResponse<Integer> saveUpdateRelation(@RequestBody @Validated List<MemberActionRelationDto> memberAgeRelationDtos);

    @PostMapping("v1/addRelation")
    public ComResponse<Integer> addRelation(@RequestBody @Validated MemberActionRelationDto memberAgeRelationDtos);

    @PostMapping("v1/addRelationWithDict")
    public ComResponse<Integer> addRelationWithDict(MemberActionRelationDto memberActionRelationDto);

    @GetMapping("v1/deleteRelation")
    public ComResponse<Integer> deleteRelation(@RequestParam("rid") Integer rid);

}

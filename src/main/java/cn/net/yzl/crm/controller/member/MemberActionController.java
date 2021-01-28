package cn.net.yzl.crm.controller.member;

import cn.net.yzl.common.entity.ComResponse;

import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelationList;
import cn.net.yzl.crm.dto.member.ActionDictDto;
import cn.net.yzl.crm.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.model.customer.ActionDict;
import cn.net.yzl.crm.model.customer.MemberActionRelation;
import cn.net.yzl.crm.service.micservice.MemberActionFeign;
import cn.net.yzl.crm.utils.ValidList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author twj
 * @version 1.0
 * @title: MemberActionController
 * @description 顾客综合行为接口
 * @date: 2021/1/22 3:07 下午
 */
@Api(tags = "顾客圈选")
@RestController(value = "memberAction")
public class MemberActionController {

    @Autowired
    private MemberActionFeign memberActionFeign;

    @ApiOperation(value = "顾客综合行为-按类型获取顾客综合行为字典")
    @GetMapping("v1/getListByType")
    public ComResponse<List<ActionDict>> getListByType(@RequestParam("type") @NotNull @Min(0) Integer type){
        return memberActionFeign.getListByType(type);
    }

    @ApiOperation(value = "顾客综合行为-保存顾客综合行为字典")
    @PostMapping("v1/memberActionSaveUpdate")
    public ComResponse<Integer> saveUpdateActionDict(@RequestBody @Validated ValidList<ActionDictDto> ageDictDtos){
        ageDictDtos.forEach(x->{
            x.setCreator(QueryIds.userNo.get());
        });
        return memberActionFeign.saveUpdateActionDict(ageDictDtos);
    }

    @ApiOperation(value = "顾客综合行为-获取顾客所有综合行为属性")
    @GetMapping("v1/getRelationByMemberCard")
    public ComResponse<List<MemberActionRelationList>> getRelationByMemberCard(@RequestParam("cardNo") @NotBlank String cardNo){
        return memberActionFeign.getRelationByMemberCard(cardNo);
    }

    @ApiOperation(value = "顾客综合行为-获取顾客一类综合行为属性")
    @GetMapping("v1/getRelationByMemberCardAndType")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCardAndType(@RequestParam("cardNo") @NotBlank String cardNo,@RequestParam("type")  @NotNull @Min(0) Integer type){
        return memberActionFeign.getRelationByMemberCardAndType(cardNo,type);
    }

    @ApiOperation(value = "顾客综合行为-保存顾客综合行为")
    @PostMapping("v1/memberAgeRelationSaveUpdate")
    public ComResponse<Integer> saveUpdateRelation(@RequestBody @Validated ValidList<MemberActionRelationDto> memberAgeRelationDtos){
        memberAgeRelationDtos.forEach(x->{
            x.setCreator(QueryIds.userNo.get());
        });
        return memberActionFeign.saveUpdateRelation(memberAgeRelationDtos);
    }

    @ApiOperation(value = "顾客综合行为-单条新增顾客综合行为")
    @PostMapping("v1/addRelation")
    public ComResponse<Integer> addRelation(@RequestBody @Validated MemberActionRelationDto memberAgeRelationDtos){
        memberAgeRelationDtos.setCreator(QueryIds.userNo.get());
        return memberActionFeign.addRelation(memberAgeRelationDtos);
    }


    @ApiOperation(value = "客户行为关联-客户综合行为手动新增录入关联")
    @PostMapping("v1/addRelationWithDict")
    public ComResponse<Integer> addRelationWithDict(@RequestBody @Validated MemberActionRelationDto memberAgeRelationDtos){
        memberAgeRelationDtos.setCreator(QueryIds.userNo.get());
        return memberActionFeign.addRelationWithDict(memberAgeRelationDtos);
    }

    @ApiOperation(value = "顾客综合行为-单条删除顾客综合行为")
    @GetMapping("v1/deleteRelation")
    public ComResponse<Integer> deleteRelation(@RequestParam("rid")  @NotNull @Min(0) Integer rid){
        return memberActionFeign.deleteRelation(rid);
    }
}

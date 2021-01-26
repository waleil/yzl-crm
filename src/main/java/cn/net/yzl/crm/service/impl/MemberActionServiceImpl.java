//package cn.net.yzl.crm.service.impl;
//
//import cn.net.yzl.common.entity.ComResponse;
//import cn.net.yzl.crm.dto.member.ActionDictDto;
//import cn.net.yzl.crm.dto.member.MemberActionRelationDto;
//import cn.net.yzl.crm.model.customer.ActionDict;
//import cn.net.yzl.crm.model.customer.MemberActionRelation;
//import cn.net.yzl.crm.service.micservice.MemberActionFeign;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class MemberActionServiceImpl implements MemberActionFeign {
//
//    @Autowired
//    private MemberActionFeign memberActionFeign;
//
//    @Override
//    public ComResponse<List<ActionDict>> getListByType(Integer type) {
//        ComResponse<List<ActionDict>> listByType = memberActionFeign.getListByType(type);
//        return listByType;
//    }
//
//    @Override
//    public ComResponse<Integer> memberAgeSaveUpdate(List<ActionDictDto> ageDictDtos) {
//        ComResponse<Integer> integerComResponse = memberActionFeign.memberAgeSaveUpdate(ageDictDtos);
//        return integerComResponse;
//    }
//
//    @Override
//    public ComResponse<List<MemberActionRelation>> getRelationByMemberCard(String cardNo) {
//        ComResponse<List<MemberActionRelation>> relationByMemberCard = memberActionFeign.getRelationByMemberCard(cardNo);
//        return relationByMemberCard;
//    }
//
//    @Override
//    public ComResponse<List<MemberActionRelation>> getRelationByMemberCardAndType(String cardNo, Integer type) {
//        ComResponse<List<MemberActionRelation>> relationByMemberCardAndType = memberActionFeign.getRelationByMemberCardAndType(cardNo, type);
//        return relationByMemberCardAndType;
//    }
//
//    @Override
//    public ComResponse<Integer> saveUpdateRelation(List<MemberActionRelationDto> memberAgeRelationDtos) {
//        ComResponse<Integer> integerComResponse = memberActionFeign.saveUpdateRelation(memberAgeRelationDtos);
//        return integerComResponse;
//    }
//
//    @Override
//    public ComResponse<Integer> addRelation(MemberActionRelationDto memberAgeRelationDtos) {
//        ComResponse<Integer> integerComResponse = memberActionFeign.addRelation(memberAgeRelationDtos);
//        return integerComResponse;
//    }
//
//    @Override
//    public ComResponse<Integer> deleteRelation(Integer rid) {
//        ComResponse<Integer> integerComResponse = memberActionFeign.deleteRelation(rid);
//        return null;
//    }
//
//
//}

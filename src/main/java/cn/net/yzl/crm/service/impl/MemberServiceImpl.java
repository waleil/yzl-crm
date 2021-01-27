package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.service.MemberService;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.ProductFien;
import cn.net.yzl.product.model.db.DiseaseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberFien memberFien;
    @Autowired
    private ProductFien productFien;


//    @Override
//    public List<Map<Integer, Object>> productClassiService(String pid) {
//        GeneralResult<List<DiseaseBean>> listGeneralResult = productFien.productClassiService(pid);
//        List<DiseaseBean> list = listGeneralResult.getData();
//        List<Map<Integer,Object>> listMap = new ArrayList<>();
//        if(list != null &&  list.size()>0){
//            for(DiseaseBean dies:list){
//                Map<Integer,Object> mp = new HashMap<>();
//                mp.put(dies.getId(),dies.getName());
//                listMap.add(mp);
//            }
//        }
//        return listMap;
//    }
//
//    @Override
//    public GeneralResult<Page<Member>> listPage(MemberSerchConditionDTO memberDto) {
//        GeneralResult<Page<Member>> result = memberFien.listPage(memberDto);
//        return result;
//    }
}

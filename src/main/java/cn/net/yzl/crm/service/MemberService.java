package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.dto.MemberSerchDTO;

import cn.net.yzl.crm.model.OrderMember;

import java.util.List;
import java.util.Map;

public interface MemberService {
    //顾客列表查询病症分类
    List<Map<Integer,Object>>productClassiService(String pid);
    //查询顾客列表
    GeneralResult<Page<Member>> listPage(MemberSerchDTO memberDto);
}

package cn.net.yzl.crm.service.impl;


import cn.net.yzl.crm.dao.IBaseDAO;
import cn.net.yzl.crm.dao.OrderMemberMapper;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.IOrderMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMemberService implements IOrderMemberService {

    @Autowired
    private OrderMemberMapper dao;

    @Override
    public IBaseDAO<OrderMember, Integer> getDao() {
        return dao;
    }
}
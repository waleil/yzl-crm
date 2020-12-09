package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.dao.CustomerCrowdGroupMapper;
import cn.net.yzl.crm.model.CustomerCrowdGroup;
import cn.net.yzl.crm.service.CustomerCrowdGroupService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CustomerCrowdGroupServiceImpl implements CustomerCrowdGroupService {


    @Autowired
    private CustomerCrowdGroupMapper mapper;

    @Override
    public PageInfo<CustomerCrowdGroup> findPage(Map<String, Object> params) {
        params.putIfAbsent("currentPage", 1);
        params.putIfAbsent("pageSize", 10);

        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageMethod.startPage(currentPage,pageSize );
        List<CustomerCrowdGroup> list = mapper.selectList(params);
        return  new PageInfo<>(list);
    }


    @Override
    public Optional<CustomerCrowdGroup> getById(String id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Integer insert(CustomerCrowdGroup vo) {
        return mapper.insert(vo);
    }

    @Override
    public Integer update(CustomerCrowdGroup vo) {
        return mapper.update(vo);
    }

}

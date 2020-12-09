package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.CustomerCrowdGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerCrowdGroupMapper {
    Integer deleteById(String id);

    Integer insert(CustomerCrowdGroup record);

    CustomerCrowdGroup selectById(String id);

    Integer update(CustomerCrowdGroup record);

    List<CustomerCrowdGroup> selectList(@Param("params") Map<String,Object> params);
}
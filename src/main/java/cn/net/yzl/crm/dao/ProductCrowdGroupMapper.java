package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.ProductCrowdGroup;

public interface ProductCrowdGroupMapper {
    int deleteByPrimaryKey(Integer rId);

    int insert(ProductCrowdGroup record);

    int insertSelective(ProductCrowdGroup record);

    ProductCrowdGroup selectByPrimaryKey(Integer rId);

    int updateByPrimaryKeySelective(ProductCrowdGroup record);

    int updateByPrimaryKey(ProductCrowdGroup record);
}
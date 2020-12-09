package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.ExpressCompany;

public interface ExpressCompanyMapper {
    int deleteById(Integer id);

    int insert(ExpressCompany record);

    ExpressCompany selectById(Integer id);

    int update(ExpressCompany record);

}
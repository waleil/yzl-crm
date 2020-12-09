package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.ExpressWaybill;

public interface ExpressWaybillMapper {
    int deleteByPrimaryKey(Integer rId);

    int insert(ExpressWaybill record);

    int insertSelective(ExpressWaybill record);

    ExpressWaybill selectByPrimaryKey(Integer rId);

    int updateByPrimaryKeySelective(ExpressWaybill record);

    int updateByPrimaryKey(ExpressWaybill record);
}
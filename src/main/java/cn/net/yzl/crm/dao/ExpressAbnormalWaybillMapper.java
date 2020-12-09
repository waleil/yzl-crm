package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.ExpressAbnormalWaybill;

public interface ExpressAbnormalWaybillMapper {
    int deleteByPrimaryKey(Integer rId);

    int insert(ExpressAbnormalWaybill record);

    int insertSelective(ExpressAbnormalWaybill record);

    ExpressAbnormalWaybill selectByPrimaryKey(Integer rId);

    int updateByPrimaryKeySelective(ExpressAbnormalWaybill record);

    int updateByPrimaryKey(ExpressAbnormalWaybill record);
}
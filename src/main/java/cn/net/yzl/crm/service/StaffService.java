package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.ehr.MarketTargetDto;
import cn.net.yzl.crm.dto.staff.OrderCriteriaDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.order.model.vo.order.OderListResDTO;

import java.util.List;

/**
 * 员工业务层业务层
 */
public interface StaffService {

    /**
     * 根据员工编号获取员工画像基本信息
     * @param staffNo
     * @return
     */
    StaffImageBaseInfoDto getStaffImageBaseInfoByStaffNo(String staffNo);


    /**
     * 根据员工编号员工商品旅程
     * @param staffNo
     * @return
     */
    Page<StaffProdcutTravelDto> getStaffProductTravel(String staffNo, Integer pageNo, Integer pageSize);


    /**
     * 根据员工编号员工订单列表
     * @param req
     * @return
     */
    ComResponse<Page<OderListResDTO>> getStaffOrderList(OrderCriteriaDto req);

    /**
     * 根据员工编号员工顾客列表
     * @param staffNo
     * @return
     */
    Page<CustomerDto> getCustomerListByStaffNo(String staffNo, Integer pageNo, Integer pageSize);

    /**
     * 根据员工工号数组查询员工列表
     * @param
     * @return
     */
    ComResponse<List<StaffDetail>> getDetailsListByNo(List<String> list);

    ComResponse<MarketTargetDto> getMarketTarget();
}

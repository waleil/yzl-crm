package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.staff.OrderDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;

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
    Page<StaffProdcutTravelDto> getStaffProductTravel(Integer staffNo, Integer pageNo, Integer pageSize);


    /**
     * 根据员工编号员工订单列表
     * @param staffNo
     * @return
     */
    Page<OrderDto> getStaffOrderList(String staffNo, Integer timeType, Integer status, Integer pageNo, Integer pageSize);

    /**
     * 根据员工编号员工顾客列表
     * @param staffNo
     * @return
     */
    Page<CustomerDto> getCustomerListByStaffNo(Integer staffNo, Integer pageNo, Integer pageSize);

}

package cn.net.yzl.crm.service;

import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;

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

}

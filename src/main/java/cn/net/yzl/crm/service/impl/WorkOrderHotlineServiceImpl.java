package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.YLoggerUtil;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.ehr.StaffQueryDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.workorder.WorkOrderHotlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 热点工单
 */
@Service
public class WorkOrderHotlineServiceImpl implements WorkOrderHotlineService {

    @Autowired
    private EhrStaffClient EhrStaffClient;
    /**
     * 热线工单：热线工单管理（可分配员工）
     * @param getDistributionStaffDTO
     */
    @Override
    public ComResponse<Page<EhrStaff>> getDistributionStaff(GetDistributionStaffDTO getDistributionStaffDTO) {
        YLoggerUtil.infoLog("热线工单管理（可分配员工）Request",String.format("getDistributionStaffDTO=%s",getDistributionStaffDTO));
        //根据员工编码获取部门
        ComResponse<Page<EhrStaff>> ehrStaffPage = null;
        ComResponse<StaffImageBaseInfoDto> detailsByNo = EhrStaffClient.getDetailsByNo(getDistributionStaffDTO.getStaffNo());
        StaffImageBaseInfoDto data = detailsByNo.getData();
        if(null != data){
            Integer departId = data.getDepartId();//部门id
            if(null != departId){
                //根据部门获取可分配员工
                StaffQueryDto staffQueryDto = new StaffQueryDto();
                staffQueryDto.setDepartId(departId);
                staffQueryDto.setPageNo(getDistributionStaffDTO.getPageNo());
                staffQueryDto.setPageNo(getDistributionStaffDTO.getPageSize());
                ehrStaffPage = EhrStaffClient.getStaffListByPage(staffQueryDto);
                }
            }
        YLoggerUtil.infoLog("热线工单管理（可分配员工）Response",String.format("ehrStaffPage=%s",ehrStaffPage));
        return ehrStaffPage;
    }

}
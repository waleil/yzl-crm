package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.service.workorder.WorkOrderHotlineService;
import org.springframework.stereotype.Service;

/**
 * 热点工单
 */
@Service
public class WorkOrderHotlineServiceImpl implements WorkOrderHotlineService {

//    @Autowired
//    private EhrStaffClient EhrStaffClient;
    /**
     * 智能工单：热线工单管理-可分配员工
     * @param getDistributionStaffDTO
     */
//    @Override
//    public ComResponse<Page<EhrStaff>> getDistributionStaff(GetDistributionStaffDTO getDistributionStaffDTO) {
//        YLoggerUtil.infoLog("智能工单：热线工单管理-可分配员工Request", JsonUtil.toJsonStr(getDistributionStaffDTO));
//        //根据员工编码获取部门
//        ComResponse<StaffImageBaseInfoDto> detailsByNo = EhrStaffClient.getDetailsByNo(getDistributionStaffDTO.getStaffNo());
//        YLoggerUtil.infoLog("根据员工编码获取部门 Response", JsonUtil.toJsonStr(detailsByNo));
//        StaffImageBaseInfoDto data = detailsByNo.getData();
//        if(null == data){
//            return ComResponse.fail(ComResponse.ERROR_STATUS,detailsByNo.getMessage());
//        }
//        Integer departId = data.getDepartId();//部门id
//        if(null == departId){
//            return ComResponse.fail(ComResponse.ERROR_STATUS,"暂无部门数据");
//        }
//        //根据部门获取可分配员工
//        StaffQueryDto staffQueryDto = new StaffQueryDto();
//        staffQueryDto.setDepartId(departId);
//        staffQueryDto.setPageNo(getDistributionStaffDTO.getPageNo());
//        staffQueryDto.setPageSize(getDistributionStaffDTO.getPageSize());
//        YLoggerUtil.infoLog("根据部门获取可分配员工 Request", JsonUtil.toJsonStr(staffQueryDto));
//        ComResponse<Page<EhrStaff>> ehrStaffPage = EhrStaffClient.getStaffListByPage(staffQueryDto);
//        YLoggerUtil.infoLog("智能工单：热线工单管理-可分配员工Response",JsonUtil.toJsonStr(ehrStaffPage));
//        return ehrStaffPage;
//    }

}

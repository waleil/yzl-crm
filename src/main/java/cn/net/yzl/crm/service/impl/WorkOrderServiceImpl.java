package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.common.util.YLoggerUtil;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.ehr.StaffQueryDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.workorder.WorkOrderService;
import cn.net.yzl.workorder.common.Constant;
import cn.net.yzl.workorder.model.dto.WorkOrderFlowDTO;
import cn.net.yzl.workorder.model.dto.WorkOrderReceiveDTO;
import cn.net.yzl.workorder.model.dto.WorkOrderReceiveUpdateDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 热点工单
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private EhrStaffClient EhrStaffClient;

    @Autowired
    private WorkOrderClient workOrderClient;
    /**
     * 智能工单：热线工单管理-可分配员工
     * @param getDistributionStaffDTO
     */
    @Override
    public ComResponse<Page<EhrStaff>> getDistributionStaff(GetDistributionStaffDTO getDistributionStaffDTO) {
        YLoggerUtil.infoLog("智能工单：热线工单管理-可分配员工Request", JsonUtil.toJsonStr(getDistributionStaffDTO));
        //根据员工编码获取部门
        ComResponse<StaffImageBaseInfoDto> detailsByNo = EhrStaffClient.getDetailsByNo(getDistributionStaffDTO.getStaffNo());
        YLoggerUtil.infoLog("根据员工编码获取部门 Response", JsonUtil.toJsonStr(detailsByNo));
        StaffImageBaseInfoDto data = detailsByNo.getData();
        if(null == data){
            return ComResponse.fail(ComResponse.ERROR_STATUS,detailsByNo.getMessage());
        }
        Integer departId = data.getDepartId();//部门id
        if(null == departId){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"暂无部门数据");
        }
        //根据部门获取可分配员工
        StaffQueryDto staffQueryDto = new StaffQueryDto();
        staffQueryDto.setDepartId(departId);
        staffQueryDto.setPageNo(getDistributionStaffDTO.getPageNo());
        staffQueryDto.setPageSize(getDistributionStaffDTO.getPageSize());
        YLoggerUtil.infoLog("根据部门获取可分配员工 Request", JsonUtil.toJsonStr(staffQueryDto));
        ComResponse<Page<EhrStaff>> ehrStaffPage = EhrStaffClient.getStaffListByPage(staffQueryDto);
        YLoggerUtil.infoLog("智能工单：热线工单管理-可分配员工Response",JsonUtil.toJsonStr(ehrStaffPage));
        return ehrStaffPage;
    }

    @Override
    public ComResponse<Void> receiveUsers(List<WorkOrderFlowDTO> list) {
        WorkOrderReceiveDTO workOrderReceiveDTO = new WorkOrderReceiveDTO();
        if (null != list && list.size() > 0) {
            // 按员工号查询员工信息
            WorkOrderReceiveUpdateDTO receiveUpdateDTO = new WorkOrderReceiveUpdateDTO();
            String staffNo = QueryIds.userNo.get();
            if(StringUtils.isNotEmpty(staffNo)){
                ComResponse<StaffImageBaseInfoDto> sresponse = EhrStaffClient.getDetailsByNo(staffNo);
                StaffImageBaseInfoDto staffInfo = sresponse.getData();
                receiveUpdateDTO.setStaffNo(staffInfo.getStaffNo());
                receiveUpdateDTO.setStaffName(staffInfo.getName());
                receiveUpdateDTO.setDeptId(staffInfo.getDepartId());
                receiveUpdateDTO.setStaffLevel(staffInfo.getPostLevelName());
                workOrderReceiveDTO.setReceiveUpdateDTO(receiveUpdateDTO);
            }
            for (WorkOrderFlowDTO workOrderFlowDTO : list) {
                workOrderFlowDTO.setCreateId(QueryIds.userNo.get());
                workOrderFlowDTO.setCreateName(QueryIds.userName.get());
                workOrderFlowDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
            }
            workOrderReceiveDTO.setWorkOrderFlows(list);
        }
        return workOrderClient.receiveUsers(workOrderReceiveDTO);
    }

}

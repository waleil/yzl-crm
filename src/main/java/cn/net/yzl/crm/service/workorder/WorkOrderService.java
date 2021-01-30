package cn.net.yzl.crm.service.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.workorder.model.dto.WorkOrderFlowDTO;

import java.util.List;

/**
 * 热点工单
 */
public interface WorkOrderService {

    /**
     * 智能工单：热线工单管理-可分配员工
     * @param getDistributionStaffDTO
     */
    ComResponse<Page<EhrStaff>> getDistributionStaff(GetDistributionStaffDTO getDistributionStaffDTO);

    /**
     * 待领取顾客池
     * @param list
     * @return
     */
    ComResponse<Boolean> receiveUsers(List<WorkOrderFlowDTO> list);
}

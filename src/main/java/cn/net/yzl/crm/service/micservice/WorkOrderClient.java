package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.member.CallInfoDTO;
import cn.net.yzl.crm.dto.staff.CallnfoCriteriaTO;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="work-order-api", url = "${api.gateway.url}/workorderServer")
public interface WorkOrderClient {

    /**
     * 获取员工通话记录
     * @param callnfoCriteriaTO
     * @return
     */
    @PostMapping("/callinfo/v2/list")
    ComResponse<Page<StaffCallRecord>> getCallRecordByStaffNo(CallnfoCriteriaTO callnfoCriteriaTO);

    /**
     * 获取通话记录
     * @param callInfoDTO
     * @returnR
     */
    @PostMapping("/callinfo/v2/list")
    ComResponse<Page<StaffCallRecord>> getCallRecord(CallInfoDTO callInfoDTO);
}

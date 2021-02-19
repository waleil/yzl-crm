package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.member.CallInfoDTO;
import cn.net.yzl.crm.dto.staff.CallnfoCriteriaTO;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import cn.net.yzl.crm.dto.workorder.MemberFirstCallDetailsDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="work-order-api", url = "${api.gateway.url}/workorderServer")
//@FeignClient(name="work-order-api", url = "127.0.0.1:4602")
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

    @ApiOperation(value = "顾客首次呼入画像，查询")
    @GetMapping("/callManage/v1/getCallInDetailsByMemberCard")
    public ComResponse<MemberFirstCallDetailsDTO> getCallInDetailsByMemberCard(@RequestParam("memberCard") String memberCard);
}

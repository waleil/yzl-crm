package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.dto.IsHandInDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 上交规则
 */
@FeignClient(name = "turnRulnClient", url = "${api.gateway.url}/workorderServer/turnRuln")
//@FeignClient(name = "turnRulnClient", url = "127.0.0.1:4602/turnRuln")
public interface TurnRulnClient {


    /**
     * 智能工单-我的回访工单-上交规则-查询通话记录表某段时间未接通记录是否超出上交规则
     *
     * @param IsHandInDTO
     * @return
     */
    @PostMapping(value = "v1/callInfoCount")
    ComResponse<Boolean> callInfoCount(IsHandInDTO IsHandInDTO);

    /**
     * 智能工单-我的回访工单-上交规则-超出维护客户上限查询
     *
     * @return
     */
    @PostMapping(value = "v1/mCustomerLExceeded")
    ComResponse<Boolean> mCustomerLExceeded(IsHandInDTO isHandInDTO);

    /**
     * 智能工单-我的回访工单-上交规则-员工放弃自取顾客
     *
     * @return
     */
    @PostMapping(value = "v1/eGiveUpTakingCustomersByThemselves")
    ComResponse<Boolean> eGiveUpTakingCustomersByThemselves(IsHandInDTO isHandInDTO);

    /**
     * 智能工单-我的回访工单-上交规则-超时回访
     *
     * @param isHandInDTO
     * @return
     */
    @PostMapping(value = "v1/overtimeReturnVisit")
    ComResponse<Boolean> overtimeReturnVisit(IsHandInDTO isHandInDTO);

    /**
     * 智能工单-我的回访工单-自取规则上交
     *
     * @param isHandInDTO
     * @return
     */
    @PostMapping("v1/rulesHandedIn")
    ComResponse<Void> rulesHandedIn(IsHandInDTO isHandInDTO);

    /**
     * 智能工单-我的回访工单-查询上交规则
     *
     * @return
     * @param ruleType
     * @param deptType
     * @param isUse
     * @param isDel
     */
    @GetMapping("v1/submissionRules")
    ComResponse<List<WorkOrderRuleConfigBean>> submissionRules(@RequestParam("ruleType") Integer ruleType,@RequestParam("deptType") Integer deptType,@RequestParam("isUse") Integer isUse,@RequestParam("isDel") Integer isDel);
}

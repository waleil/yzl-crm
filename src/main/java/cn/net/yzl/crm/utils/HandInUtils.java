package cn.net.yzl.crm.utils;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.OrderClient;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.dto.IsHandInDTO;
import cn.net.yzl.workorder.model.enums.RuleDescriptionEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HandInUtils {

    @Autowired
    private WorkOrderClient workOrderClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private StaffService staffService;


    /**
     * 空号停机
     *
     * @param emptyNumberShutdown
     * @return
     */
    public Boolean emptyNumberShutdown(IsHandInDTO emptyNumberShutdown) {
        return Boolean.TRUE;
    }

    /**
     * 无法联系
     *
     * @param emptyNumberShutdown
     * @param data
     * @return
     */
    public Boolean unableToContact(IsHandInDTO emptyNumberShutdown, List<WorkOrderRuleConfigBean> data) {
        WorkOrderRuleConfigBean wORCBean = null;
        for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
            if (RuleDescriptionEnums.HOTLINE_CENTER_RULE_TWO.getCode().equals(workOrderRuleConfigBean.getId())) {
                wORCBean = workOrderRuleConfigBean;
            }
        }
        String paramsValue = wORCBean.getParamsValue();
        emptyNumberShutdown.setParamValue(paramsValue);
        ComResponse<Boolean> booleanComResponse = workOrderClient.callInfoCount(emptyNumberShutdown);
        if (!booleanComResponse.getData()) {
            return Boolean.FALSE;
        }
        //TODO
        return Boolean.TRUE;
    }

    /**
     * 客户拒访
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean customerRefusedToVisit(IsHandInDTO isHandInDTO) {
        if (!isHandInDTO.getApplyUpMemo().equals("顾客要求不需要回访")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 客户退款
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean customerRefund(IsHandInDTO isHandInDTO) {
        return Boolean.TRUE;
    }

    /**
     * 休眠客户
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean dormantCustomers(IsHandInDTO isHandInDTO) {
        return Boolean.TRUE;
    }

    /**
     * 超出维护客户上限
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean mCustomerLExceeded(IsHandInDTO isHandInDTO, List<WorkOrderRuleConfigBean> data) {
        WorkOrderRuleConfigBean wORCBean = null;
        for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
            if (RuleDescriptionEnums.HOTLINE_CENTER_RULE_TWO.getCode().equals(workOrderRuleConfigBean.getId())) {
                wORCBean = workOrderRuleConfigBean;
            }
        }
        String paramsValue = wORCBean.getParamsValue();
        isHandInDTO.setParamValue(paramsValue);
        StaffImageBaseInfoDto staffImageBaseInfoByStaffNo = staffService.getStaffImageBaseInfoByStaffNo(isHandInDTO.getStaffNo());
        isHandInDTO.setPostId(staffImageBaseInfoByStaffNo.getPostId());
        isHandInDTO.setPostLevelId(staffImageBaseInfoByStaffNo.getPostLevelId());
        return workOrderClient.mCustomerLExceeded(isHandInDTO).getData();

    }

    /**
     * 员工放弃自取顾客
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean eGiveUpTakingCustomersByThemselves(IsHandInDTO isHandInDTO) {
        return workOrderClient.eGiveUpTakingCustomersByThemselves(isHandInDTO).getData();
    }

    /**
     * 超时回访
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean overtimeReturnVisit(IsHandInDTO isHandInDTO, List<WorkOrderRuleConfigBean> data) {
        WorkOrderRuleConfigBean wORCBean = null;
        for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
            if (RuleDescriptionEnums.HOTLINE_CENTER_RULE_TWO.getCode().equals(workOrderRuleConfigBean.getId())) {
                wORCBean = workOrderRuleConfigBean;
            }
        }
        String paramsValue = wORCBean.getParamsValue();
        isHandInDTO.setParamValue(paramsValue);
        return workOrderClient.overtimeReturnVisit(isHandInDTO).getData();
    }
}
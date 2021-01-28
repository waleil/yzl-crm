package cn.net.yzl.crm.utils;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.workorder.TurnRulnClient;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class HandInUtils {

    @Autowired
    private WorkOrderClient workOrderClient;

    @Autowired
    private TurnRulnClient callInfoCount;

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private StaffService staffService;


    /**
     * 空号停机
     *
     * @param emptyNumberShutdown
     * @param wORCBean
     * @return
     */
    public Boolean emptyNumberShutdown(IsHandInDTO emptyNumberShutdown, WorkOrderRuleConfigBean wORCBean) {
        String paramsValue = wORCBean.getParamsValue();
        Date date = new Date();
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_DETAIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -Integer.valueOf(paramsValue));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_DETAIL_FORMAT);
        return orderSearchClient.getSignOrderStatus(emptyNumberShutdown.getMemberCard(), startDate, endDate).getData().getNearNoSignStatus();

    }

    /**
     * 无法联系
     *
     * @param emptyNumberShutdown
     * @param wORCBean
     * @return
     */
    public Boolean unableToContact(IsHandInDTO emptyNumberShutdown, WorkOrderRuleConfigBean wORCBean) {
        String paramsValue = wORCBean.getParamsValue();
        emptyNumberShutdown.setParamValue(paramsValue);
        ComResponse<Boolean> booleanComResponse = callInfoCount.callInfoCount(emptyNumberShutdown);
        if (!booleanComResponse.getData()) {
            return Boolean.FALSE;
        }
        Date date = new Date();
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_DETAIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] split = paramsValue.split(",");
        calendar.add(Calendar.MONTH, -Integer.valueOf(split[2]));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_DETAIL_FORMAT);
        return orderSearchClient.getSignOrderStatus(emptyNumberShutdown.getMemberCard(), startDate, endDate).getData().getNearNoContinuityShoppingStatus();
    }

    /**
     * 客户拒访
     *
     * @param isHandInDTO
     * @param wORCBean
     * @return
     */
    public Boolean customerRefusedToVisit(IsHandInDTO isHandInDTO, WorkOrderRuleConfigBean wORCBean) {
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
    public Boolean customerRefund(IsHandInDTO isHandInDTO, WorkOrderRuleConfigBean wORCBean) {
        String paramsValue = wORCBean.getParamsValue();
        Date date = new Date();
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_DETAIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -Integer.valueOf(paramsValue));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_DETAIL_FORMAT);
        return orderSearchClient.getSignOrderStatus(isHandInDTO.getMemberCard(), startDate, endDate).getData().getIsGuestComplaint();
    }

    /**
     * 休眠客户
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean dormantCustomers(IsHandInDTO isHandInDTO, WorkOrderRuleConfigBean wORCBean) {
        String paramsValue = wORCBean.getParamsValue();
        Date date = new Date();
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_DETAIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -Integer.valueOf(paramsValue));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_DETAIL_FORMAT);
        return orderSearchClient.getSignOrderStatus(isHandInDTO.getMemberCard(), startDate, endDate).getData().getNearNoSignStatus();
    }

    /**
     * 超出维护客户上限
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean mCustomerLExceeded(IsHandInDTO isHandInDTO, WorkOrderRuleConfigBean wORCBean) {

        String paramsValue = wORCBean.getParamsValue();
        isHandInDTO.setParamValue(paramsValue);
        StaffImageBaseInfoDto staffImageBaseInfoByStaffNo = staffService.getStaffImageBaseInfoByStaffNo(isHandInDTO.getStaffNo());
        isHandInDTO.setPostId(staffImageBaseInfoByStaffNo.getPostId());
        isHandInDTO.setPostLevelId(staffImageBaseInfoByStaffNo.getPostLevelId());
        return callInfoCount.mCustomerLExceeded(isHandInDTO).getData();

    }

    /**
     * 员工放弃自取顾客
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean eGiveUpTakingCustomersByThemselves(IsHandInDTO isHandInDTO) {
        return callInfoCount.eGiveUpTakingCustomersByThemselves(isHandInDTO).getData();
    }

    /**
     * 超时回访
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean overtimeReturnVisit(IsHandInDTO isHandInDTO, WorkOrderRuleConfigBean wORCBean) {

        String paramsValue = wORCBean.getParamsValue();
        isHandInDTO.setParamValue(paramsValue);
        return callInfoCount.overtimeReturnVisit(isHandInDTO).getData();
    }
}
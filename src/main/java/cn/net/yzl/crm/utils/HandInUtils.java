package cn.net.yzl.crm.utils;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.workorder.TurnRulnClient;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.dto.IsHandInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;

@Component
public class HandInUtils{

//    @Autowired
//    private WorkOrderClient workOrderClient;

    @Autowired
    private TurnRulnClient turnRulnClient;

    @Autowired
    private OrderSearchClient orderSearchClient;

//    @Autowired
//    private StaffService staffService;

    @Autowired
    private EhrStaffClient ehrStaffClient;


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
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] split = paramsValue.split(",");
        if(null == split || 0 == split.length){
            return  Boolean.FALSE;
        }
        calendar.add(Calendar.MONTH, -Integer.valueOf(split[0]));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_FORMAT);
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
        ComResponse<Boolean> booleanComResponse = turnRulnClient.callInfoCount(emptyNumberShutdown);
        if (!booleanComResponse.getData()) {
            return Boolean.FALSE;
        }
        Date date = new Date();
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] split = paramsValue.split(",");
        if(null == split || 0 == split.length){
            return  Boolean.FALSE;
        }
        calendar.add(Calendar.MONTH, -Integer.valueOf(split[2]));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_FORMAT);
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
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] split = paramsValue.split(",");
        if(null == split || 0 == split.length){
            return  Boolean.FALSE;
        }
        calendar.add(Calendar.MONTH, -Integer.valueOf(split[0]));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_FORMAT);
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
        String endDate = DateFormatUtil.dateToString(date, DateFormatUtil.UTIL_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] split = paramsValue.split(",");
        if(null == split || 0 == split.length){
            return  Boolean.FALSE;
        }
        calendar.add(Calendar.MONTH, -Integer.valueOf(split[0]));
        Date time = calendar.getTime();
        String startDate = DateFormatUtil.dateToString(time, DateFormatUtil.UTIL_FORMAT);
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
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(isHandInDTO.getStaffNo());
        StaffImageBaseInfoDto data = detailsByNo.getData();
        if(null == data){
            return Boolean.FALSE;
        }
        ComResponse<DepartDto> departById = ehrStaffClient.getDepartById(data.getDepartId());
        DepartDto data1 = departById.getData();
        if(null == data1){
            return Boolean.FALSE;
        }
        ComResponse<StaffImageBaseInfoDto> detailsByNo1 = ehrStaffClient.getDetailsByNo(data1.getLeaderNo());
        StaffImageBaseInfoDto data2 = detailsByNo1.getData();
        if(null == data2){
            return Boolean.FALSE;
        }

        isHandInDTO.setPostId(data2.getPostId());
        //用来接收部门参数
        isHandInDTO.setSouce(data2.getDepartId());
        isHandInDTO.setPostLevelId(data2.getPostLevelId());
        return turnRulnClient.mCustomerLExceeded(isHandInDTO).getData();

    }

    /**
     * 员工放弃自取顾客
     *
     * @param isHandInDTO
     * @return
     */
    public Boolean eGiveUpTakingCustomersByThemselves(IsHandInDTO isHandInDTO) {
        return turnRulnClient.eGiveUpTakingCustomersByThemselves(isHandInDTO).getData();
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
        return turnRulnClient.overtimeReturnVisit(isHandInDTO).getData();
    }
}
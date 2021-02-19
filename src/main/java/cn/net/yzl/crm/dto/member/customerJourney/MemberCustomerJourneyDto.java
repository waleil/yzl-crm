package cn.net.yzl.crm.dto.member.customerJourney;

import cn.net.yzl.crm.customer.dto.member.MemberGradeRecordDto;
import cn.net.yzl.crm.dto.workorder.MemberFirstCallDetailsDTO;
import cn.net.yzl.order.model.vo.order.PortraitOrderDetailDTO;
import cn.net.yzl.workorder.model.db.CallInfoBean;
import cn.net.yzl.workorder.model.db.WorkOrderDisposeFlowSubBean;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "MemberCustomerJourneyDto", description = "顾客旅程实体")
@Data
public class MemberCustomerJourneyDto implements Serializable {
    /**  */
	private static final long serialVersionUID = 4957765480022077476L;
	@ApiModelProperty("工单id")
    private String _id;
    @ApiModelProperty("工单号")
    private Integer workOrderCode;
    @ApiModelProperty("工单类型 1:热线工单，2回访工单,3,会员等级,4:呼入")
    private Integer workOrderType;
    @ApiModelProperty("员工部门id（热线每日更新）")
    private String deptId;
    @ApiModelProperty("员工部门名称（热线每日更新）")
    private String deptName;
    @ApiModelProperty("员工编号（热线每日更新）")
    private String staffNo;
    @ApiModelProperty("员工姓名（热线每日更新）")
    private String staffName;
    @ApiModelProperty("员工级别（热线每日更新）")
    private String staffLevel;
    @ApiModelProperty("座席已拨打次数 （热线每日更新）")
    private Integer callTimes;
    @ApiModelProperty("创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("顾客信息")
    private WorkOrderDisposeFlowSubBean workOrderDisposeFlowSubBean;
    @ApiModelProperty("通话记录集合")
    private List<CallInfoBean> callInfoBeanLists;

    @ApiModelProperty("订单信息,核弹信息,订单拒收,追单信息")
    private List<PortraitOrderDetailDTO> portraitOrderDetailList;
    @ApiModelProperty("顾客等级记录表")
    private MemberGradeRecordDto memberGradeRecordDto;

    @ApiModelProperty("首次呼入明细")
    private MemberFirstCallDetailsDTO memberFirstCallDetailsDTO;



}
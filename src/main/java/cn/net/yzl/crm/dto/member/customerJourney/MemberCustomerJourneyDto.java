package cn.net.yzl.crm.dto.member.customerJourney;

import cn.net.yzl.order.model.vo.order.PortraitOrderDetailDTO;
import cn.net.yzl.workorder.model.db.CallInfoBean;
import cn.net.yzl.workorder.model.db.WorkOrderDisposeFlowSubBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "MemberCustomerJourneyDto", description = "顾客旅程实体")
@Data
public class MemberCustomerJourneyDto implements Serializable {
    @ApiModelProperty("工单id")
    private String _id;
    @ApiModelProperty("工单号")
    private Integer workOrderCode;
    @ApiModelProperty("工单类型 1:热线工单，2回访工单")
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
    @ApiModelProperty("顾客信息")
    private WorkOrderDisposeFlowSubBean workOrderDisposeFlowSubBean;
    @ApiModelProperty("通话记录集合")
    private List<CallInfoBean> callInfoBeanLists;

    @ApiModelProperty("订单信息,核弹信息,订单拒收,追单信息")
    private List<PortraitOrderDetailDTO> portraitOrderDetailList;


}
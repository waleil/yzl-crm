package cn.net.yzl.crm.dto.workorder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 热线工单：回收
 */
@Data
public class UpdateRecyclingDTO {
    @ApiModelProperty(value = "工单编码")
    private Integer code;

    @ApiModelProperty(value = "座席是否接受工单1:已接受，2：未接受")
    private int acceptStatus;

    @ApiModelProperty(value = "申请回收状态 0：未申请，1：已申请，2:已回收")
    private int applyUpStauts;

    @ApiModelProperty(value = "员工编号")
    private String staffNo;

    @ApiModelProperty(value = "操作人")
    private String operator;

}

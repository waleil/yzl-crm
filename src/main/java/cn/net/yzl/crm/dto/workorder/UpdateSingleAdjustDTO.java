package cn.net.yzl.crm.dto.workorder;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 热线工单：单数据调整
 */
@Data
public class UpdateSingleAdjustDTO implements Serializable {

    @ApiModelProperty(value = "工单编码")
    @NotNull(message = "工单标识校验错误")
    private Integer code;

    @ApiModelProperty(value = "员工编号")
    @NotEmpty(message = "员工编号校验错误")
    private String staffNo;

    @ApiModelProperty(value = "员工名称")
    @NotEmpty(message = "员工名称校验错误")
    private String staffName;

    @ApiModelProperty(value = "操作人")
    @NotEmpty(message = "操作人校验错误")
    private String operator;

    @ApiModelProperty(value = "回拨剩余时间")
    @NotNull(message = "回拨剩余时间")
    private Integer callBackDeadline;

    //座席是否接受工单1:已接受，2：未接受
    private Integer acceptStatus;

    //分配时间
    private Date allocateTime;

    //调整次数
    private Integer transTimes;


}

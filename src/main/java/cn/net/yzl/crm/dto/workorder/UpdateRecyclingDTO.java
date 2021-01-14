package cn.net.yzl.crm.dto.workorder;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 热线工单：回收
 */
@Data
public class UpdateRecyclingDTO implements Serializable {
    @ApiModelProperty(value = "工单编码")
    @NotNull(message = "工单标识校验错误")
    private Integer code;

    @ApiModelProperty(value = "员工编号")
    @NotEmpty(message = "员工编号校验错误")
    private String staffNo;

    @ApiModelProperty(value = "操作人")
    @NotEmpty(message = "操作人校验错误")
    private String operator;

    //座席是否接受工单1:已接受，2：未接受
    private Integer acceptStatus;

    //申请回收状态 0：未申请，1：已申请，2:已回收
    private Integer applyUpStauts;
}

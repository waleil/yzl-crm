package cn.net.yzl.crm.dto.workorder;

import cn.net.yzl.workorder.model.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class GetDistributionStaffDTO extends BaseDTO<GetDistributionStaffDTO> {

    @ApiModelProperty(value = "用户编号")
    @NotNull(message = "用户编号校验错误")
    private String staffNo;
}

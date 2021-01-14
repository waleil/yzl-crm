package cn.net.yzl.crm.dto.workorder;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 热线工单：单数据调整
 */
@Data
public class UpdateMoreAdjustDTO implements Serializable {

    @Valid
    @ApiModelProperty(value = "集合")
    @NotNull(message = "集合校验错误")
    List<UpdateSingleAdjustDTO> list;

}

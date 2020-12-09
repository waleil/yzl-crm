package cn.net.yzl.crm.dto.crowdgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="更新生效失效参数类",description="参数类" )
@Data
public class UpdateInvalidateParamDTO implements Serializable {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "状态；0=失效，1=生效")
    private Integer status;
}

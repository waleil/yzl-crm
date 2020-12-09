package cn.net.yzl.crm.dto.crowdgroup;

import cn.net.yzl.crm.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@ApiModel(value="查询参数类",description="参数类" )
@Data
public class ListPageParamDTO extends PageDTO {

    @ApiModelProperty(value = "群组名称")
    private String name;

    @ApiModelProperty(value = "状态；0=失效，1=生效")
    private Integer status;

    @ApiModelProperty(value = "开始日期")
    private Date effectiveDate;

    @ApiModelProperty(value = "结束日期")
    private Date expirationDate;

}

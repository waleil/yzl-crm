package cn.net.yzl.crm.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value="分页参数类",description="参数类" )
@Data
public class PageDTO implements Serializable {

    @NotNull(message = "当前页码不能为空")
    @ApiModelProperty(value = "当前页码",required = true)
    private Integer currentPage;

    @NotNull(message = "每页条数不能为空")
    @ApiModelProperty(value = "每页条数",required = true)
    private Integer pageSize;

}

package cn.net.yzl.crm.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="分页参数类",description="参数类" )
@Data
public class PageDTO implements Serializable {

    @ApiModelProperty(value = "当前页码")
    private Integer currentPage;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

}

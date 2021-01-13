package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Advisor {

    @ApiModelProperty("广告编号")
    private String advisorNo;

    @ApiModelProperty("广告名称")
    private String advisorName;
}

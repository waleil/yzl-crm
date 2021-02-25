package cn.net.yzl.crm.dto.member.customerJourney;


import cn.net.yzl.workorder.model.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class QueryWorkOrderVo extends BaseDTO {

    @ApiModelProperty(value = "顾客会员号",required = true)
    @NotEmpty(message = "顾客会员号必填")
    private String memberCard;

    @ApiModelProperty("年")
    private String year;

    @ApiModelProperty(value="查询会员级别记录的开始时间", name = "startTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String startTime;


}
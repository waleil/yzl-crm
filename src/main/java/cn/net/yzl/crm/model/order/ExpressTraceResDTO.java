package cn.net.yzl.crm.model.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 运单轨迹
 * @Author jingweitao
 * @Date 10:06 2020/11/26
 **/
@ApiModel(value="查询物流轨迹响应类",description="响应类" )
@Data
public class ExpressTraceResDTO implements Serializable {

    @ApiModelProperty(value = "运单号")
    private String mailId; // 运单号

    @ApiModelProperty(value = "城市")
    private String city;    // 城市

    @ApiModelProperty(value = "描述")
    private String description;    // 描述

    @ApiModelProperty(value = "操作网点")
    private String site;    // 操作网点

    @ApiModelProperty(value = "状态")
    private String status;  // 状态

    @ApiModelProperty(value = "时间")
    private LocalDateTime time; // 时间


}

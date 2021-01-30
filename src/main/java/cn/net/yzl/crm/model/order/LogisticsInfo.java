package cn.net.yzl.crm.model.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        value = "订单物流轨迹跟踪",
        description = "响应类"
)
@Data
public class LogisticsInfo {

    @ApiModelProperty("运单号")
    private String mailId;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("操作网点")
    private String site;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("时间")
    private String time;

}

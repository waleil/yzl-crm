package cn.net.yzl.crm.dto.express;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="快递运单轨迹返回值类",description="返回值类" )
@Data
public class DemoExpressWaybillTraceResDTO {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "快递公司名称")
    private String expressCompanyName;

    @ApiModelProperty(value = "跟踪时间")
    private Date createTime;

    @ApiModelProperty(value = "跟踪记录")
    private String traceDesc;

}

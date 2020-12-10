package cn.net.yzl.crm.dto.express;


import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "跟踪时间")
    private Date createTime;

    @ApiModelProperty(value = "跟踪记录")
    private String traceDesc;

}

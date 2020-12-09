package cn.net.yzl.crm.dto.express;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="快递运单号返回值类",description="返回值类" )
@Data
public class ExpressWaybillResDTO {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "快递运单号")
    private String mailId;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "快递公司id")
    private String expressCompanyId;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "签收时间")
    private Date receivingTime;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;
}

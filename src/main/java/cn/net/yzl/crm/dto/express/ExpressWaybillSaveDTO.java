package cn.net.yzl.crm.dto.express;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="快递运单号保存类",description="参数类" )
@Data
public class ExpressWaybillSaveDTO {

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

}

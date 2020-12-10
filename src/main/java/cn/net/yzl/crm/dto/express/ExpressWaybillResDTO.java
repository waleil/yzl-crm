package cn.net.yzl.crm.dto.express;


import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "签收时间")
    private Date receivingTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;
}

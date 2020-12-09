package cn.net.yzl.crm.dto.express;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="快递运单异常save参数类",description="参数类" )
@Data
public class ExpressAbnormalWaybillSaveDTO {

    @ApiModelProperty(value = "唯一标识id")
    private String id;


    @ApiModelProperty(value = "'快递运单号'")
    private String mailId;

    @ApiModelProperty(value = "'订单id'")
    private String orderId;

    @ApiModelProperty(value = "单据状态；0=已赔付，1=已取消")
    private Byte billStatus;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;
}

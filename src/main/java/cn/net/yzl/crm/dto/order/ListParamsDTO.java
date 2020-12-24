package cn.net.yzl.crm.dto.order;


import cn.net.yzl.crm.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="列表参数类",description="参数类" )
@Data
public class ListParamsDTO extends PageDTO {

    @ApiModelProperty(value = "订单编号")
    private String orderNoLike;

    @ApiModelProperty(value = "顾客姓名")
    private String memberNameLike;

    @ApiModelProperty(value = "媒介类型：0=电台广播媒体，1=电视媒体")
    private Integer mediaType;

    @ApiModelProperty(value = "订单状态：0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4.物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成")
    private Integer orderStatus;

    @ApiModelProperty(value = "下单开始时间")
    private Date startTime;

    @ApiModelProperty(value = "下单结束时间")
    private Date endTime;

}

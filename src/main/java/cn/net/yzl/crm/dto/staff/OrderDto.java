package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("员工画像  订单实体")
@Getter
@Setter
public class OrderDto {

    @ApiModelProperty("订单号")
    private Integer orderNo;
    @ApiModelProperty("媒介类型")
    private Integer mediumType;
    @ApiModelProperty("媒介名称")
    private String mediumName;
    @ApiModelProperty("订单性质")
    private Integer orderType;
    @ApiModelProperty("顾客卡号")
    private Integer customerNo;
    @ApiModelProperty("顾客姓名")
    private String customerName;
    @ApiModelProperty("订单状态")
    private Integer status;
    @ApiModelProperty("下单时间")
    private String payTime;
    @ApiModelProperty("下单方式")
    private Integer payMethod;
    @ApiModelProperty("订单金额")
    private Integer payMoney;
    @ApiModelProperty("实际支付金额")
    private Integer actualPayMoney;
}

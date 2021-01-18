package cn.net.yzl.crm.model.order;

import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        value = "订单详情查询输出参数类",
        description = "参数类"
)

@Data
public class OrderInfoVO {
    @ApiModelProperty("订单基础信息")
    private OrderInfoResDTO orderInfoResDTO ;

    @ApiModelProperty("购买人信息")
    private Member member ;
}

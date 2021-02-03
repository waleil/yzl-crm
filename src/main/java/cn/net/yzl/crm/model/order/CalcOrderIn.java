package cn.net.yzl.crm.model.order;

import java.util.ArrayList;
import java.util.List;

import cn.net.yzl.order.model.vo.order.OrderDetailIn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单
 * 
 * @author zhangweiwei
 * @date 2021年2月3日,下午4:22:32
 */
@ApiModel(description = "订单")
@Setter
@Getter
@ToString
public class CalcOrderIn {
	@ApiModelProperty(hidden = true)
	private Integer total;
	@ApiModelProperty(hidden = true)
	private Integer cash;
	@ApiModelProperty(value = "使用储值金额 单位元", required = false)
	private Double amountStored;
	@ApiModelProperty(value = "使用优惠券 单位元", required = false)
	private Double amountCoupon;
	@ApiModelProperty(value = "订单明细", required = true)
	private List<OrderDetailIn> orderDetailIns = new ArrayList<OrderDetailIn>(0);
}

package cn.net.yzl.crm.model.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单
 * 
 * @author zhangweiwei
 * @date 2021年2月3日,下午5:09:54
 */
@ApiModel(description = "订单")
@Setter
@Getter
@ToString
public class CalcOrderOut {

	@ApiModelProperty(value = "订单总额，单位元", required = false)
	private Double totalAll;
	@ApiModelProperty(value = "使用优惠券 单位元", required = false)
	private Double amountCoupon;
	@ApiModelProperty(value = "使用积分抵扣 单位元", required = false)
	private Double pointsDeduction;
	@ApiModelProperty(value = "使用储值金额 单位元", required = false)
	private Double amountStored;
	@ApiModelProperty(value = "实收金额，单位元", required = false)
	private Double total;
}
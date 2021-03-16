package cn.net.yzl.crm.model.order;

import java.util.ArrayList;
import java.util.List;

import cn.net.yzl.activity.model.responseModel.ProductPriceResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class CalcOrderOut {

	@ApiModelProperty(value = "订单总额，单位元", required = false)
	private Double totalAll = 0d;
	@ApiModelProperty(value = "实收金额，单位元", required = false)
	private Double total = 0d;
	@ApiModelProperty(value = "使用优惠券 单位元", required = false)
	private Double amountCoupon = 0d;
	@ApiModelProperty(value = "使用积分抵扣 单位元", required = false)
	private Double pointsDeduction = 0d;
	@ApiModelProperty(value = "使用储值金额 单位元", required = false)
	private Double amountStored = 0d;
	@ApiModelProperty(value = "DMC接口返回的优惠金额 单位元", required = false)
	private Double productTotal = 0d;
	@ApiModelProperty(value = "订单商品明细")
	private List<ProductPriceResponse> products = new ArrayList<>(0);

}

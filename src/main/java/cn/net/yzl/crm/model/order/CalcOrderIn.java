package cn.net.yzl.crm.model.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.net.yzl.activity.model.dto.CalculateProductDto;
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
	@ApiModelProperty(value = "广告业务主键", required = false)
	private Long advertBusNo;
	@ApiModelProperty(value = "会员卡号", required = true)
	private String memberCard;
	@ApiModelProperty(value = "使用储值金额 单位元", required = false)
	private BigDecimal amountStored = BigDecimal.ZERO;
	@ApiModelProperty(value = "使用的优惠券折扣ID,针对订单使用的", required = false)
	private Integer couponDiscountIdForOrder;
	@ApiModelProperty(value = "使用的优惠券ID,针对订单使用的", required = false)
	private Integer memberCouponIdForOrder;
	@ApiModelProperty(value = "商品相关信息", required = true)
	private List<CalculateOrderProductDto> calculateProductDtos = new ArrayList<>(0);

	@Getter
	@Setter
	@ToString
	public static class CalculateOrderProductDto extends CalculateProductDto {
		@ApiModelProperty(value = "赠品标识：0=购买，1=赠送", required = false)
		private Integer giftFlag;
	}
}

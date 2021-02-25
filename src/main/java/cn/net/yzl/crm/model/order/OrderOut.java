package cn.net.yzl.crm.model.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.net.yzl.order.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 订单
 * 
 * @author zhangweiwei
 * @date 2021年1月31日,上午2:10:54
 */
@Getter
@Setter
@ApiModel(description = "订单")
public class OrderOut {
	@ApiModelProperty(value = "收货人地址")
	private String reveiverAddress;
	@ApiModelProperty(value = "收货人姓名")
	private String reveiverName;
	@ApiModelProperty(value = "收货人电话")
	private String reveiverTelphoneNo;
	@ApiModelProperty(value = "实收金额，单位元")
	private BigDecimal total = BigDecimal.ZERO;
	@ApiModelProperty(value = "账户余额，单位元")
	private BigDecimal totalMoney = BigDecimal.ZERO;
	@ApiModelProperty(value = "订单号")
	private String orderNo;
	@ApiModelProperty(value = "下单时间")
	@JsonFormat(pattern = CommonConstant.JSON_FORMAT_PATTERN, timezone = CommonConstant.JSON_FORMAT_TIMEZONE)
	private Date orderTime;
	@ApiModelProperty(value = "本次购物获得积分")
	private Integer returnPointsDeduction = 0;
	@ApiModelProperty(value = "本次购物获得优惠券")
	private List<Coupon> coupons = new ArrayList<>(0);

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ApiModel(description = "优惠券")
	public static class Coupon {
		@ApiModelProperty(value = "有效期开始时间")
		@JsonFormat(pattern = CommonConstant.JSON_FORMAT_PATTERN, timezone = CommonConstant.JSON_FORMAT_TIMEZONE)
		private Date startDate;
		@ApiModelProperty(value = "有效期结束时间")
		@JsonFormat(pattern = CommonConstant.JSON_FORMAT_PATTERN, timezone = CommonConstant.JSON_FORMAT_TIMEZONE)
		private Date endDate;
		@ApiModelProperty(value = "优惠券名称")
		private String couponName;
	}
}

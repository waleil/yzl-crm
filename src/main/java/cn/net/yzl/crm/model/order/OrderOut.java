package cn.net.yzl.crm.model.order;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单
 * 
 * @author zhangweiwei
 * @date 2021年1月31日,上午2:10:54
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "订单")
@SuppressWarnings("serial")
public class OrderOut implements Serializable {
	@ApiModelProperty(value = "收货人地址")
	private String reveiverAddress;
	@ApiModelProperty(value = "收货人姓名")
	private String reveiverName;
	@ApiModelProperty(value = "收货人电话")
	private String reveiverTelphoneNo;
	@ApiModelProperty(value = "实收金额，单位元")
	private double total;
	@ApiModelProperty(value = "账户余额，单位元")
	private double totalMoney;
	@ApiModelProperty(value = "订单号")
	private String orderNo;
}

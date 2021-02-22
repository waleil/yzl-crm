package cn.net.yzl.crm.model.order;

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
	@ApiModelProperty("广告业务主键")
	private Long advertBusNo;
	@ApiModelProperty("会员卡号")
	private String memberCard;
	@ApiModelProperty(value = "商品相关信息", required = true)
	private List<CalculateProductDto> calculateProductDtos = new ArrayList<>(0);
}

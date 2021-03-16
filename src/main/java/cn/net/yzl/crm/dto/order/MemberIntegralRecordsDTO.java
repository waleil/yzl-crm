package cn.net.yzl.crm.dto.order;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author zhouchangsong
 */
@SuppressWarnings("serial")
@Data
@ApiModel("顾客积分")
public class MemberIntegralRecordsDTO implements Serializable {

	@ExcelProperty(value = { "会员卡号" }, index = 0)
	@ApiModelProperty("会员卡号")
	private String memberCard;

	@ExcelProperty(value = { "会员名称" }, index = 1)
	@ApiModelProperty("会员名称")
	private String memberName;

	@ExcelProperty(value = { "订单编号" }, index = 2)
	@ApiModelProperty("订单编号")
	private String orderNo;

	@ExcelProperty(value = { "财务归属" }, index = 3)
	@ApiModelProperty("财务归属")
	private String financialOwnerName;

	@ExcelIgnore
	@ApiModelProperty("业务环节（0、领取 1、可用 2、冻结 3、已使用 4、失效）")
	private Integer status;

	@ExcelProperty(value = { "业务环节" }, index = 4)
	@ApiModelProperty("业务环节（0、领取 1、可用 2、冻结 3、已使用 4、失效）")
	private String statusName;

	@ExcelProperty(value = { "使用积分" }, index = 5)
	@ApiModelProperty("使用积分")
	private Integer integral;

	@ExcelIgnore
	@ApiModelProperty("积分操作类型(0：增加，1：扣减)")
	private Integer operationType;

	@ExcelProperty(value = { "创建时间" }, index = 6)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@ExcelProperty(value = { "对账时间" }, index = 7)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("对账时间")
	private Date reconciliationTime;

	public static final Map<Integer, String> STATUS_MAP = ImmutableMap.<Integer, String>builder().put(0, "领取")
			.put(1, "可用").put(2, "冻结").put(3, "已使用").put(4, "失效").build();
}

package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="订单VO类",description="订单VO类" )
@Data
public class OrderMember {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "部门表唯一标识")
    private Integer departId;

    @ApiModelProperty(value = "配送地址唯一标识")
    private Long reveiverAddressNo;

    @ApiModelProperty(value = "工单号唯一标识")
    private Long workOrderNo;

    @ApiModelProperty(value = "媒介唯一标识")
    private Long mediaNo;
    @ApiModelProperty(value = "媒介名称")
    private String mediaName;
    @ApiModelProperty(value = "媒介类型：0=电台广播媒体，1=电视媒体")
    private Integer mediaType;

    @ApiModelProperty(value = "订单活动唯一标识")
    private Long activityNo;

    @ApiModelProperty(value = "订单性质：1=正常订单，0=免审订单")
    private Integer orderNature;

    @ApiModelProperty(value = "订单状态：0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4.物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成")
    private Integer orderStatus;

    @ApiModelProperty(value = "物流状态：0.待发货 1.已发货  2.已揽件 3.运途中 4.已签收 5.拒签 6.问题件")
    private Integer logisticsStatus;

    @ApiModelProperty(value = "订单总金额 单位分")
    private Integer amountOrder;

    @ApiModelProperty(value = "是否是首单产品 1:首单 0:非首单")
    private Integer firstOrderFlag;

    @ApiModelProperty(value = "配送费用 单位分")
    private Integer amountDelivery;

    @ApiModelProperty(value = "应收金额 单位分")
    private Integer amountReceivable;

    @ApiModelProperty(value = "实收金额 单位分")
    private Integer amountActual;

    @ApiModelProperty(value = "使用红包金额 单位分")
    private Integer amountRedEnvelope;

    @ApiModelProperty(value = "使用优惠券 单位分")
    private Integer amountCoupon;

    @ApiModelProperty(value = "使用积分抵扣 单位分")
    private Integer pointsDeduction;

    @ApiModelProperty(value = "返回红包金额 单位分")
    private Integer returnAmountRedEnvelope;

    @ApiModelProperty(value = "返回优惠券 单位分")
    private Integer returnAmountCoupon;

    @ApiModelProperty(value = "返回积分 单位分")
    private Integer returnPointsDeduction;

    @ApiModelProperty(value = "顾客表唯一标识")
    private Integer memberNo;

    @ApiModelProperty(value = "顾客姓名")
    private String memberName;

    @ApiModelProperty(value = "顾客卡号")
    private Integer memberCardNo;

    @ApiModelProperty(value = "是否开票：0=否，1=是")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "付款方式：0=货到付款，1=款到发货")
    private Integer payType;

    @ApiModelProperty(value = "商品件数")
    private Integer productNumber;

    @ApiModelProperty(value = "订单备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人编号")
    private String updateCode;



}
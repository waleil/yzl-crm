package cn.net.yzl.crm.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouchangsong
 */
@Data
@ApiModel("顾客优惠券")
public class MemberCouponDTO implements Serializable {
    private static final long serialVersionUID = 3001248090787063693L;

    @ApiModelProperty("会员卡号")
    private String memberCard;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("状态（0、领取 1、可用 2、冻结 3、已使用 4、失效）")
    private Integer status;

    @ApiModelProperty("会员名称")
    private String memberName;

    @ApiModelProperty("财物归属")
    private String financialOwnerName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("创建时间 ")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("对账时间")
    private Date reconciliationTime;

    @ApiModelProperty("优惠券码")
    private Long couponBusNo;

    @ApiModelProperty("面值")
    private BigDecimal reduceAmount;

}

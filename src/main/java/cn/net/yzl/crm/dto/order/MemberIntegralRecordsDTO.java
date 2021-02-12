package cn.net.yzl.crm.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhouchangsong
 */
@Data
@ApiModel("顾客积分")
public class MemberIntegralRecordsDTO implements Serializable {

    private static final long serialVersionUID = -3702666065908941041L;

    @ApiModelProperty("会员卡号")
    private String memberCard;

    @ApiModelProperty("会员名称")
    private String memberName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("财物归属")
    private String financialOwnerName;

    @ApiModelProperty("业务环节（0、领取 1、可用 2、冻结 3、已使用 4、失效）")
    private Integer status;

    @ApiModelProperty("使用积分")
    private Integer integral;

    @ApiModelProperty("积分操作类型(0：增加，1：扣减)")
    private Integer operationType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("对账时间")
    private Date reconciliationTime;
}

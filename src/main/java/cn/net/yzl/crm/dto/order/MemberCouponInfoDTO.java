package cn.net.yzl.crm.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengyu
 */
@Data
@ApiModel("顾客优惠券使用情况")
public class MemberCouponInfoDTO {
    @ApiModelProperty("会员卡号")
    private String   memberCard;
    @ApiModelProperty("可用的优惠券数量")
    private Integer   memberCouponSize;
    @ApiModelProperty("可用积分")
    private Integer  memberIntegral;
    @ApiModelProperty("可用红包")
    private Integer memberRedBag;
}

package cn.net.yzl.crm.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhouchangsong
 */
@Data
@ApiModel("拒收单新增对象")
public class OrderRejectionAddDTO implements Serializable {
    private static final long serialVersionUID = 7672131073218627860L;

    @ApiModelProperty("订单号")
    @NotEmpty(message = "订单号不可为空")
    private String orderNo;

    @ApiModelProperty("退库方式：1.返回原仓，2.其他仓库")
    @NotNull(message = "退库方式不可为空")
    private Integer rejectType;

    @ApiModelProperty("仓库号")
    private String storeNo;
}

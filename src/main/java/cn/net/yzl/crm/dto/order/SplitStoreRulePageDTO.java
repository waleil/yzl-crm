package cn.net.yzl.crm.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouchangsong
 */
@Data
@ApiModel("订单分仓规则分页对象")
public class SplitStoreRulePageDTO implements Serializable {
    private static final long serialVersionUID = 5460294024714484364L;

    @ApiModelProperty("仓库编号")
    private String storeNo;
    @ApiModelProperty("仓库名称")
    private String storeName;
    @ApiModelProperty("仓库类型，1:主仓,2:售后,3:残损仓")
    private Integer sType;
    @ApiModelProperty("经营类型，1:自建仓库,2:租赁,3逻辑仓")
    private Integer mType;
    @ApiModelProperty("覆盖省份")
    private String provinceNames;
}

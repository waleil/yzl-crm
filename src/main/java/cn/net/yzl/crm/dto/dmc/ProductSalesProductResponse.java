package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 商品促销活动 - 商品信息
 * </p>
 *
 * @author liuChangFu
 * @since 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProductSalesProductResponse")
public class ProductSalesProductResponse {


    @ApiModelProperty(value = "商品code")
    private String productCode;

    @ApiModelProperty(value = "商品类型（0：单商品，1：套餐）")
    private Integer productType;

    @ApiModelProperty(value = "活动库存", example = "100")
    private Integer activityProductStock;

}

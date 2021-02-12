package cn.net.yzl.crm.model.meal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ProductVO
 * @description 商品信息
 * @date: 2021/1/21 11:05 下午
 */
@Data
public class ProductVO {
    @ApiModelProperty(name = "productCode",value = "商品ID(product唯一标识)")
    private String productCode;

    @ApiModelProperty(name = "name",  value = "商品名称")
    private String name;

    @ApiModelProperty(name = "salePriceD",  value = "市场价(售卖价),以元为单位")
    private Double salePriceD;

    @ApiModelProperty(name = "stock", value = "库存,-1代表不限制库存")
    private Integer stock;

    @ApiModelProperty(name = "limitDownPriceD",  value = "最低价格,所有的优惠扣减不能低于此价格,以元为单位")
    private Double limitDownPriceD;

    @ApiModelProperty(name = "unit", value = "计量单位")
    private String unit;

    @ApiModelProperty(name = "totalUseNum", value = "规格")
    private Integer totalUseNum;

    @ApiModelProperty(name = "barCode", value = "商品条形码")
    private String barCode;
    @ApiModelProperty(name = "giftFlag", value = "是否套餐赠品(0代表否,1代表是)")
    private Integer giftFlag;
    @ApiModelProperty(name = "num", value = "套餐要求产品数量")
    private Integer num;
}

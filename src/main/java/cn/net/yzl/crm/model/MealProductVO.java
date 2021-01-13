package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "MealProductVO",description = "套餐商品信息")
public class MealProductVO {

    @ApiModelProperty(value = "套餐编码",name = "mealNo")
    private String mealNo;

    @ApiModelProperty(value = "商品编码",name = "productCode" ,required = true)
    private String productCode;

    @ApiModelProperty(value = "商品数量",name = "productNum",required = true)
    private Integer productNum;

    @ApiModelProperty(value = "是否赠品(0代表否,1代表是)",name = "mealGiftFlag",required = true)
    private Integer mealGiftFlag;

}
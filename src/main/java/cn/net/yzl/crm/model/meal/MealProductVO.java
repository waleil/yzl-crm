package cn.net.yzl.crm.model.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MealProduct
 * @description 套餐关联商品信息
 * @date: 2021/1/21 11:01 下午
 */
@Data
public class MealProductVO {
    @ApiModelProperty(name = "mealNo", value = "套餐ID")
    private String mealNo;

    @ApiModelProperty(name = "name", value = "套餐名称")
    private String name;

    @ApiModelProperty(name = "pirce", value = "套餐价格")
    @JsonIgnore
    private Integer price;

    @ApiModelProperty(name = "discountPrice", value = "套餐优惠折扣价")
    @JsonIgnore
    private Integer discountPrice;

    @ApiModelProperty(name = "priceD", value = "套餐价格(元)")
    private Double priceD;

    @ApiModelProperty(name = "discountPriceD", value = "套餐优惠折扣价(元)")
    private Double discountPriceD;
    @ApiModelProperty(name = "productVOS", value = "套餐管理的商品信息")
    private List<ProductVO> productVOS;
}

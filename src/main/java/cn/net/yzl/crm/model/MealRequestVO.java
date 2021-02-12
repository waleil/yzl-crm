package cn.net.yzl.crm.model;

import cn.net.yzl.product.model.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author wanghuasheng
 * @version 1.0
 * @title: ProductMealVO
 * @description 商品套餐编辑实体类
 * @date: 2021/1/10 14:40 下午
 */
@Data
@ApiModel(value = "MealRequestVO",description = "套餐信息")
public class MealRequestVO extends BaseObject {

    @ApiModelProperty(value = "套餐编码",name = "mealNo")
    private String mealNo;

    @ApiModelProperty(value = "套餐名称",name = "name",required = true)
    @NotBlank(message = "套餐名称不能为空！")
    private String name;

    @ApiModelProperty(value = "套餐价,以分为单位",name = "price")
    private Integer price;

    @ApiModelProperty(value = "套餐最低优惠折扣价,以分为单位",name = "discountPrice")
    private Integer discountPrice;

    @ApiModelProperty(name = "priceD", required = true, value = "套餐价,以元为单位")
    @DecimalMin(value = "0",message = "套餐价,不能为空")
    private Double priceD;

    @ApiModelProperty(name = "discountPriceD", required = true, value = "套餐最低优惠折扣价,以元为单位")
    @DecimalMin(value = "0",message = "套餐最低优惠折扣价,不能为空")
    private Double discountPriceD;

    @ApiModelProperty(value = "套餐描述",name = "descri",required = true)
    private String descri;

    @ApiModelProperty(value = "销售开始时间",name = "saleStartTime",required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartTime;

    @ApiModelProperty(value = "销售结束时间",name = "saleEndTime",required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleEndTime;

    @ApiModelProperty(name = "mealProducts", value = "套餐商品信息")
    private List<MealProductVO> mealProducts;

    @ApiModelProperty(name = "url",value = "编辑时如不上传图片则在此字段返回url")
    private String url;

}

package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 商品下发任务关联商品表
 * </p>
 *
 * @author liuChangFu
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RelationProductDto", description = "关联商品表")
public class RelationProductDto {

    @ApiModelProperty(value = "关联商品表主键id")
    private Integer id;

    @ApiModelProperty(value = "商品或者套餐名称")
    private String productName;

    @ApiModelProperty(value = "商品或者套餐唯一标识")
    private String productCode;

    @ApiModelProperty(value = "商品类型（0：单商品，1：套餐）")
    private Integer productType;

}

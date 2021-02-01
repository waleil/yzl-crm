package cn.net.yzl.crm.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: liuChangFu
 * @date: 2021/1/16 23:17
 * @desc: product的基本信息摘要
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduceDto {

    @ApiModelProperty("商品唯一编码")
    private String productCode;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品主图片")
    private String imageUrl;

    @ApiModelProperty("市场价(售卖价)单位为元")
    private String salePriceD;

    @ApiModelProperty("库存,-1代表不限制库存")
    private Integer stock;

    @ApiModelProperty("总药量")
    private Integer totalUseNum;

    @ApiModelProperty("计量单位")
    private String unit;

}

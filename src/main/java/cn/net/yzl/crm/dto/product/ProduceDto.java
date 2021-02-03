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

    @ApiModelProperty(value = "商品唯一编码")
    private String productCode;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品主图片")
    private String imageUrl;

    @ApiModelProperty(value = "市场价(售卖价)单位为元")
    private String salePriceD;

    @ApiModelProperty(value = "每天服用次数")
    private Integer oneToTimes;

    @ApiModelProperty(value = "每次使用次数")
    private Integer oneUseNum;

    @ApiModelProperty(value = "禁用人群")
    private String forbidden;

    @ApiModelProperty(value = "适用人群")
    private String applicable;

    @ApiModelProperty(value = "主治病症")
    private String diseaseName;

    @ApiModelProperty(value = "主要原料")
    private String rawStock;

    @ApiModelProperty(value = "包装单位")
    private String packagingUnit;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "商品总药量-规格")
    private String totalUseNum;

}

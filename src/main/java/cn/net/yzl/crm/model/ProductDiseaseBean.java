package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductDiseaseBean implements Serializable {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("病症id")
    private Integer diseaseId;

    @ApiModelProperty("病症类型(0代表主治,1代表延伸)")
    private Boolean dtype;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
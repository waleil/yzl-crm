package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BrandBeanTO implements Serializable {

    @ApiModelProperty(value = "品牌实体")
    private BrandBean brandBean;

    @ApiModelProperty(value = "改品牌下产品数量")
    private Integer productCount;

}

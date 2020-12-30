package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryTO implements Serializable {

    @ApiModelProperty(value = "分类实体")
    public CategoryBean categoryBean;

    @ApiModelProperty(value = "分类下产品数量")
    public Integer productCount;

    @ApiModelProperty(value = "分类下属性值列表")
    public List<AttributeBean> attributeBeans;

}

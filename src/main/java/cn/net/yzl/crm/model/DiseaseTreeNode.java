package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DiseaseTreeNode implements Serializable {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("病症名称")
    private String name;

    @ApiModelProperty("该病症下的子节点")
    private List<DiseaseTreeNode> childNodes;

    @ApiModelProperty("该病症关联的商品")
    private List<ProductMainInfoBean> products;

}

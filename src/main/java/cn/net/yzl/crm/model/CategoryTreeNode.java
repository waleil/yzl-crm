package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryTreeNode implements Serializable {

    @ApiModelProperty("分类id")
    private Integer id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类下的子节点")
    private List<CategoryTreeNode> childNodes;

}

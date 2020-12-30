package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryTreeNode implements Serializable {

    private Integer id;

    private String name;

    private List<CategoryTreeNode> childNodes;

}

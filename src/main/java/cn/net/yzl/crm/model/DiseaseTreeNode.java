package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DiseaseTreeNode implements Serializable {

    private Integer id;

    private String name;

    private List<DiseaseTreeNode> childNodes;

    private List<ProductMainInfoBean> products;

}

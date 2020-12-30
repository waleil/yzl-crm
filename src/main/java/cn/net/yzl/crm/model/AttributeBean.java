package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品属性基本信息
 */
@Data
public class AttributeBean implements Serializable {

    private Integer id;
    private String name;
    private String values;
    private String classifyId;
    private String classifyName;
    private Integer retrievalFlag;
    private Integer attributeType;
    private Integer sort;
    private Integer delFlag;

    private List<cn.net.yzl.crm.model.AttributeValueBean> attributeValueBeanList;

    private Date createTime;
    private Date updateTime;
}

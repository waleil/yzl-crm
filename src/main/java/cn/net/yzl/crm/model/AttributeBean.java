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
    private String name;//属性名称
    private String values;//属性值(多个属性值按逗号切割)
    private String classifyId;//二级分类id
    private String classifyName;//二级分类名称
    private Integer retrievalFlag;//能否进行检索(0代表不检索,1代表检索,3代表范围检索)
    private Integer attributeType;//属性是否可选(0代表唯一属性,1代表单选属性,2复选属性,3手工录入)
    private Integer sort;//排序
    private Integer delFlag;//是否删除(0代表否,1代表是)

    private List<AttributeValueBean> attributeValueBeanList;

    private Date createTime;
    private Date updateTime;
}

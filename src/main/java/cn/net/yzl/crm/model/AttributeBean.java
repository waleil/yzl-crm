package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品属性基本信息
 */
@Data
public class AttributeBean implements Serializable {

    @ApiModelProperty(value = "属性id")
    private Integer id;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性值")
    private String values;

    @ApiModelProperty(value = "分类id")
    private String classifyId;

    @ApiModelProperty(value = "分类名称")
    private String classifyName;

    @ApiModelProperty(value = "能否进行检索(0代表不检索,1代表检索,3代表范围检索)")
    private Integer retrievalFlag;

    @ApiModelProperty(value = "属性是否可选(0代表唯一属性,1代表单选属性,2复选属性,3手工录入")
    private Integer attributeType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否删除(0代表否,1代表是)")
    private Integer delFlag;

    @ApiModelProperty(value = "属性值集合")
    private List<AttributeValueBean> attributeValueBeanList;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}

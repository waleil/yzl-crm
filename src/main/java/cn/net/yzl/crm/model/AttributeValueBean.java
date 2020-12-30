package cn.net.yzl.crm.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AttributeValueBean implements Serializable {

    @ApiModelProperty(value = "属性值id")
    private Integer id;

    @ApiModelProperty(value = "所属属性id")
    private Integer attributeId;

    @ApiModelProperty(value = "属性值")
    private String avalue;

    @ApiModelProperty(value = "是否删除(0代表否,1代表是)")
    private Integer delFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}

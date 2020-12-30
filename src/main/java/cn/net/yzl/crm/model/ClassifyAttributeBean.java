package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ClassifyAttributeBean implements Serializable {

    @ApiModelProperty("分类id")
    private Integer classifyId;

    @ApiModelProperty("病症id")
    private Integer attributeId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
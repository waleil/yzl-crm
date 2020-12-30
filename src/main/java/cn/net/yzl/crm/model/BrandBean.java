package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BrandBean implements Serializable {

    @ApiModelProperty(value = "品牌id")
    private Integer id;

    @ApiModelProperty(value = "品牌名称")
    private String name;

    @ApiModelProperty(value = "是否启用(0:否,1:是)")
    private Boolean status;

    @ApiModelProperty(value = "品牌LOGO路径")
    private String brandUrl;

    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "品牌故事")
    private String descri;

    @ApiModelProperty(value = "添加操作员id")
    private String createNo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改操作员id")
    private String updateNo;

    @ApiModelProperty(value = "更新时期")
    private Date updateTime;

}
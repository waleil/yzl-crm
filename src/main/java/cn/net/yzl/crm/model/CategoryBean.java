package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CategoryBean implements Serializable{

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "pid,一类分类为0")
    private Integer pid;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否显示(0代表否,1代表是)")
    private Boolean displayFlag;

    @ApiModelProperty(value = "是否显示在移动端(0代表否,1代表是)")
    private Boolean displayAppFlag;

    @ApiModelProperty(value = "状态(0:无效,1:有效)")
    private Boolean status;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "分类图标路径")
    private String imageUrl;

    @ApiModelProperty(value = "关键词")
    private String keyWord;

    @ApiModelProperty(value = "描述")
    private String descri;

    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;

    @ApiModelProperty(value = "添加操作员id")
    private String createNo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改操作员id")
    private String updateNo;

    @ApiModelProperty(value = "更新时期")
    private Date updateTime;

}
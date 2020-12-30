package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DiseaseBean implements Serializable {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("病症名称")
    private String name;

    @ApiModelProperty("父类id")
    private Integer pid;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建人员编号")
    private String createNo;

    @ApiModelProperty("修改人员编号")
    private String updateNo;

    @ApiModelProperty("是否删除")
    private Boolean delFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
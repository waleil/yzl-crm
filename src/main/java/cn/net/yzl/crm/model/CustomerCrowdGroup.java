package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="顾客人群圈选Vo类",description="顾客人群圈选Vo类" )
@Data
public class CustomerCrowdGroup {


    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ApiModelProperty(value = "群组名称")
    private String name;

    @ApiModelProperty(value = "群组描述")
    private String description;

    @ApiModelProperty(value = "状态；0=未启用，1=启用")
    private Integer status;

    @ApiModelProperty(value = "生效时间")
    private Date effectiveDate;

    @ApiModelProperty(value = "失效时间")
    private Date expirationDate;

    @ApiModelProperty(value = "是否删除；0=否，1=是")
    private Integer isDel;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;


}
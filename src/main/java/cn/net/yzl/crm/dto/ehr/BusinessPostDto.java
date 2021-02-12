package cn.net.yzl.crm.dto.ehr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * resume_edu
 * @author 
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@ApiModel(value = "BusinessPostDto", description = "业务属性岗位信息实体")
public class BusinessPostDto implements Serializable {
    @ApiModelProperty(value = "业务属性id", name = "bussinessAtrrCode")
    protected Integer bussinessAtrrCode;
    @ApiModelProperty(value = "业务属性名称", name = "bussinessAtrrName")
    protected String bussinessAtrrName;
    @ApiModelProperty(value = "岗位id", name = "postId")
    protected Integer postId;
    @ApiModelProperty(value = "岗位名称", name = "postName")
    protected String postName;
    @ApiModelProperty(value = "岗位级别id", name = "postLevelId")
    protected Integer postLevelId;
    @ApiModelProperty(value = "岗位级别名称", name = "postLevelName")
    protected String postLevelName;
  
}
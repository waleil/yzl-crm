package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * ehr岗位列表返回封装实体
 */
@Getter
@Setter
@ApiModel("ehr岗位列表返回封装实体")
public class EhrPostDto {

    @ApiModelProperty("岗位code")
    private  Integer attrCode;
    @ApiModelProperty("岗位名称")
    private  String attrName;
    @ApiModelProperty("部门id")
    private  Integer departId;
    @ApiModelProperty("部门名称")
    private  String departName;
    @ApiModelProperty("岗位职责")
    private  String duty;
    @ApiModelProperty("主键")
    private  Integer id;
    @ApiModelProperty("岗位id")
    private  Integer postId;
    @ApiModelProperty("")
    private  Integer jobNum;
    @ApiModelProperty("名称")
    private  String name;
    @ApiModelProperty("员工编号")
    private  Integer staffNum;
}

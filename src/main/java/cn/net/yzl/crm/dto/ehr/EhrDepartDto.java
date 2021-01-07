package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("通过员工编号获取部门响应实体")
@Getter
@Setter
public class EhrDepartDto {
    @ApiModelProperty("部门id")
    private Integer id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("父id")
    private Integer pid;
    @ApiModelProperty("部门领导id")
    private String leaderNo;
    @ApiModelProperty("部门领导姓名")
    private String leaderName;
    @ApiModelProperty("组织描述")
    private String desc;
    @ApiModelProperty("排序")
    private Integer order;
    private List<EhrDepartDto> children;
    private Integer level;
    private Integer financeDepartId;
    private String financeDepartName;
    private Integer attrCode;

}

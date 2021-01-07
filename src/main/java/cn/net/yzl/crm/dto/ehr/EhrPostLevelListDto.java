package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Ehr获取部门岗位级别列表实体
 */
@Getter
@Setter
@ApiModel("Ehr获取部门岗位级别列表实体")
public class EhrPostLevelListDto {
    @ApiModelProperty("岗位级别id")
    private String id;
    @ApiModelProperty("岗位级别名称")
    private String name;
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("岗位名称")
    private String postName;
}

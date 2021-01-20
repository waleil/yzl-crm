package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffQueryDto  extends PageDto {

   @ApiModelProperty("姓名/工号")
    private String params;
    @ApiModelProperty("岗位id")
    private Integer postId;
    @ApiModelProperty("部门id")
    private Integer departId;
    @ApiModelProperty("在岗状态")
    private Integer postStatusCode;
    @ApiModelProperty("岗位级别")
    private Integer postLevelId;

    @ApiModelProperty(value = "属性(1:正编,2:外包)")
    private Integer nature;
}

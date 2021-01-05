package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("EHR返回排班结果")
@Getter
@Setter
public class StaffScheduleResutDto {

    @ApiModelProperty("部门id")
    private String departId;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("岗位级别id")
    private String postLevelId;
    @ApiModelProperty("岗位级别名称")
    private String postLevelName;
    @ApiModelProperty("岗位名称")
    private String postName;
    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("员工工号")
    private String staffNo;
    @ApiModelProperty("排班字节")
    private String statusBit;
    @ApiModelProperty("排班状态(用4个字节表示上班状态,1:上班,0:表示休息)")
    private String statusBitStr;
    @ApiModelProperty("时间")
    private String time;
    @ApiModelProperty("l类型(1:全天班,2:半天班)")
    private String type;
}

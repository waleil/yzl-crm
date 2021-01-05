package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("员工排班查询条件")
@Getter
@Setter
public class StaffScheduleQueryDto extends EhrPageInfo {
    @ApiModelProperty(name = "部门id",notes="需要测试数据传10")
    private Integer departId;
    @ApiModelProperty("工号/姓名/联系电话")
    private String params;
    @ApiModelProperty(name="时间",notes = "格式format yyyy-mm")
    private String time;
//    @ApiModelProperty("开始时间")
//    private String timeEnd;
//    @ApiModelProperty("结束时间")
//    private String timeStart;
}

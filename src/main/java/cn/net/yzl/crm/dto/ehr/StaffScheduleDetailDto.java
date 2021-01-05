package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * staff_schedule
 * @author 
 */
@Data
@ApiModel(value = "StaffScheduleDetailDto", description = "员工排班详细实体信息")
public class StaffScheduleDetailDto extends StaffScheduleResutDto {


    @ApiModelProperty(value = "是否可抢标识(0:可抢,1:不可抢)", name = "robbedStr")
    private String robbedStr;
    @ApiModelProperty(value = "本月可以休息天数", name = "restTotalDays")
    private Integer restTotalDays;

    @ApiModelProperty(value = "本月已休息天数", name = "restedDays")
    private Integer restedDays;
}
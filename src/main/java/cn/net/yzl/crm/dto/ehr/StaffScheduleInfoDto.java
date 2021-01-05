package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("排班信息列表Model")
@Getter
@Setter
public class StaffScheduleInfoDto {
    @ApiModelProperty("排班集合")
    private List<StaffScheduleResutDto> items;
    @ApiModelProperty("分页信息")
    private EhrPageInfo pageParam;
}

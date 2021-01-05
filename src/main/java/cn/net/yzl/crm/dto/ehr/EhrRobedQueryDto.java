package cn.net.yzl.crm.dto.ehr;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("排班后抢班入参实体")
@Setter
@Getter
public class EhrRobedQueryDto {

    @ApiModelProperty("排班信息id")
    private  String  staffScheduleId;
    @ApiModelProperty("时间(几号,例如:1)")
    private  String  time;
    @ApiModelProperty("类型(1:全天班,2:半天班)")
    private  String  type;
}

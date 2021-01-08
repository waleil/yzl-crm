package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * EHR返回员工实体对象
 */
@Getter
@Setter
@ApiModel("EHR返回员工实体对象")
public class EhrStaffDto {

    @ApiModelProperty("员工列表")
    private List<EhrStaff> items;
    @ApiModelProperty("分页参数")
    private PageDto pageParam;
}

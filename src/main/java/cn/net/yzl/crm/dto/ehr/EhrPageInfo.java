package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * EHR分页信息
 */
@Getter
@Setter
public class EhrPageInfo {
    @ApiModelProperty("当前页")
    private Integer pageNo;
    @ApiModelProperty("每页的数量")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer pageTotal;
}

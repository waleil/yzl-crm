package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 分页参数
 */
@Getter
@Setter
public class PageDto {
    @ApiModelProperty("当前页")
    private  Integer pageNo;
    @ApiModelProperty("每页参数")
    private  Integer pageSize;
    @ApiModelProperty("一共多少页")
    private  Integer pageTotal;
}

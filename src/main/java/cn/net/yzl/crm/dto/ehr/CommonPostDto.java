package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * ehr岗位列表返回封装实体
 */
@Getter
@Setter
@ApiModel("CommonPostDto")
public class CommonPostDto {


    @ApiModelProperty("主键")
    private  Integer id;
    @ApiModelProperty("岗位名称")
    private  String name;

}

package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: liuChangFu
 * @date: 2021/2/20 21:50
 * @desc: 试算返回用户详情
 **/
@Data
@ApiModel("试算返回用户详情")
public class CalculationUerDetail {

    @ApiModelProperty(value = "用户工号")
    private String userNo;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "性別")
    private Integer sex;

    @ApiModelProperty(value = "部门")
    private String departName;

    @ApiModelProperty(value = "岗位")
    private String postName;

    @ApiModelProperty(value = "级别")
    private String postLevelName;

    @ApiModelProperty(value = "在职状态")
    private String postStatusCodeStr;

}

package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: liuChangFu
 * @date: 2020/12/30 0:58
 **/
@Data
@ApiModel(value = "StaffDetailDto对象", description = "员工详情信息")
public class StaffDetailDto {

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "英语名")
    private String enName;

    @ApiModelProperty(value = "真实姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户编码")
    private String userNo;
}

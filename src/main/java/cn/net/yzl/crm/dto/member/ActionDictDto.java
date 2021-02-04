package cn.net.yzl.crm.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * action_dict
 * @author 
 */
@ApiModel(value = "actionDictDto", description = "顾客综合行为字典")
@Data
@Valid
public class ActionDictDto implements Serializable {
    @ApiModelProperty("主键,新增时不需填写;一类字典全部删除时,传id为0和type有值的数据")
    @Min(0)
    private Integer id;

    @ApiModelProperty("名称")
    @NotBlank
    private String name;

    @ApiModelProperty("综合行为类型：1、方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
    @NotNull
    @Min(0)
    private Byte type;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值2")
    private String value2;

    @ApiModelProperty(value = "创建人",hidden = true)
    private String creator;

    @ApiModelProperty(value ="操作人",hidden = true)
    private String updator;



}
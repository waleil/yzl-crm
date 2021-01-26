package cn.net.yzl.crm.model.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * action_dict
 * @author 
 */
@Data
public class ActionDict implements Serializable {

    @ApiModelProperty("字典编号")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("综合行为类型：1、方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
    private Byte type;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值2")
    private String value2;


}
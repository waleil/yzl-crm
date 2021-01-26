package cn.net.yzl.crm.model.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * member_action_relation
 * @author 
 */
@Data
public class MemberActionRelation implements Serializable {

    @ApiModelProperty("编号")
    private Integer id;

    @ApiModelProperty("顾客卡号")
    private String memberCard;

    @ApiModelProperty("字典id")
    private Integer did;

    @ApiModelProperty("字典值名")
    private String dname;

    @ApiModelProperty("综合行为类型：1方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
    private Integer type;

    @ApiModelProperty("值1")
    private String value;

    @ApiModelProperty("值2")
    private String value2;


    private static final long serialVersionUID = 1L;

}
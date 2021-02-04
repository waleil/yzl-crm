package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangyan
 * @date: 2021/01/26 18:00
 * @desc: 商品积分列表出参
 **/
@Data
@ApiModel("会员等级列表出参")
public class MemberLevelResponse {


    @ApiModelProperty(value = "会员等级(数字 0-9 对应等级1到等级10)")
    private Integer memberLevelGrade;

    @ApiModelProperty(value = "会员等级名称")
    private String memberLevelName;

}

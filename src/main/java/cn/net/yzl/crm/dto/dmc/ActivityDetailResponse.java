package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: liuChangFu
 * @date: 2021/01/20 15:04
 * @desc: 商品促销活动列表出参
 **/
@Data
@ApiModel("商品促销活动详情出参")
public class ActivityDetailResponse {

    @ApiModelProperty(value = "活动业务主键")
    private Long busNo;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动类型（0：集团，1：渠道）")
    private Integer activityType;

}

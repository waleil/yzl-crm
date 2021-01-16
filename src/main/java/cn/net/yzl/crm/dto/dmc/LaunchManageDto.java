package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: liuChangFu
 * @date: 2020/12/28 15:04
 * @desc: 投放管理详情出参
 **/
@Data
@ApiModel("投放管理详情出参")
public class LaunchManageDto {

    @ApiModelProperty(value = "媒介类型（0：电视媒体，1：广播电台媒体）")
    private Integer relationBusType;

    @ApiModelProperty(value = "关联媒介业务主键")
    private Long relationBusNo;

    @ApiModelProperty(value = "广告类型（0：品牌广告，1：效果广告）", name = "advertType", example = "0/1")
    private Integer advertType;

    @ApiModelProperty(value = "广告名称", name = "advertName", example = "双11变通大促销")
    private String advertName;

    @ApiModelProperty(value = "播放状态(0：待开始，1：播放中，2：已结束)")
    private Integer playStatus;

    @ApiModelProperty(value = "媒介名称", name = "mediaName")
    private String mediaName;

}

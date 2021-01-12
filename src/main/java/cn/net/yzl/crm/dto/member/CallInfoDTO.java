package cn.net.yzl.crm.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取沟通记录查询实体")
@Data
public class CallInfoDTO {
    @ApiModelProperty(value = "通话状态 0、正常 1、无效 2、掐线 3、未接听")
    private Integer status;

    @ApiModelProperty(value = "通话开始时间")
    private String startTime;

    @ApiModelProperty(value = "通话结束时间")
    private String endTime;

    @ApiModelProperty(value = "顾客会员号")
    private String memberCard;

    @ApiModelProperty(value = "员工号")
    private String staffNo;

    @ApiModelProperty(value = "当前页",required = true)
    private int pageNo;

    @ApiModelProperty(value = "每页显示条数",required = true)
    private int pageSize;

    @ApiModelProperty(value = "分页查询开始位置")
    private Integer startIndex;

    public Integer getStartIndex() {
        if(this.startIndex == null) {
            pageNo=pageNo < 1 ? 1 : pageNo;
            this.startIndex=(pageNo - 1) * pageSize;
        }
        return this.startIndex == null ? 0 : this.startIndex;
    }
    //是否需要分页
    public boolean isNeedPaging() {
        return pageSize > 0 && pageNo > 0;
    }
}

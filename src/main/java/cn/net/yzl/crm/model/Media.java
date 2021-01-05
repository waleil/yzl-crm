package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Media {
    private int id;

    @ApiModelProperty("业务主键")
    private String busNo;
    @ApiModelProperty("关联公司业务主键")
    private String companyBusNo;
    @ApiModelProperty("媒介类型 0:电视媒体, 1:广播电台媒体，2：社区媒体，3：户外媒体，4：印刷媒体，5：互联网媒体，6：电商站内流量媒体")
    private int mediaType;
    @ApiModelProperty("媒介名称")
    private String mediaName;
    @ApiModelProperty("是否删除 1:已删除 0:未删除")
    private int isDel;
    private String createTime;
    private int createId;
    private String updateTime;
    private int updateId;
}

package cn.net.yzl.crm.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="省份返回值类",description="返回值类" )
@Data
public class ProvinceTResDTO {
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "行政区编号")
    private Integer code;

    @ApiModelProperty(value = "国家编号")
    private Integer countryId;

    @ApiModelProperty(value = "大区名称")
    private String zname;

    @ApiModelProperty(value = "简称")
    private String aname;

    @ApiModelProperty(value = "拼音简称")
    private String pname;

    @ApiModelProperty(value = "单字简称")
    private String abbr;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
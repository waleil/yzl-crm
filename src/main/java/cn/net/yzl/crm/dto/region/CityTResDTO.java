package cn.net.yzl.crm.dto.region;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="省份返回值类",description="返回值类" )
@Data
public class CityTResDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "城市名称")
    private String name;

    @ApiModelProperty(value = "省的主键")
    private Integer pvcId;

    @ApiModelProperty(value = "是否为省会城市")
    private Integer pvcCapital;

    @ApiModelProperty(value = "座机电话区号")
    private Integer phonePrefixCode;

    @ApiModelProperty(value = "座机电话长度")
    private Integer phoneLength;

    @ApiModelProperty(value = "城市拼音简写")
    private String enAbbr;

    @ApiModelProperty(value = "邮编")
    private String zip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;





}
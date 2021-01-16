package cn.net.yzl.crm.dto.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Album {

    @ApiModelProperty(name = "name",value = "相册的名称")
    @NotEmpty(message = "相册名称不能为空！")
    private String name ;

    @ApiModelProperty(name = "type",value = "相册类型（0：图片库，1：视频库）")
    @NotNull(message = "相册类型不能为空！")
    @Range(min = 0,max = 1,message = "相册类型超出范围！")
    private Byte type;

    @ApiModelProperty(name = "descri",value = "描述")
    private String descri;

    @ApiModelProperty(name = "sort",value = "排序")
    private Integer sort;

}

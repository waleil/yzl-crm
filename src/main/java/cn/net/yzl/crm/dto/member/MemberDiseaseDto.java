package cn.net.yzl.crm.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("顾客病症")
@Data
public class MemberDiseaseDto {

    @ApiModelProperty("顾客卡号")
    @NotBlank
    private String memberCard;

    @ApiModelProperty("病症父id")
    @NotNull
    @Min(0)
    private Integer parDiseaseId;

    @ApiModelProperty("病症id")
    @Min(0)
    private Integer diseaseId;

    @ApiModelProperty("病症名称")
    @NotBlank
    private String diseaseName;

    @ApiModelProperty(value = "操作人",hidden = true)
    private String createNo;
}

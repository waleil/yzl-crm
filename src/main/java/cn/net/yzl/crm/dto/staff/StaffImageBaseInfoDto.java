package cn.net.yzl.crm.dto.staff;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 员工画像基本信息实体类
 */
@ApiModel("员工画像基本信息实体类")
@Getter
@Setter
public class StaffImageBaseInfoDto {
    @ApiModelProperty("地址详情")
    private String address;
    @ApiModelProperty("头像")
    private String imgUrl;
    @ApiModelProperty("部门id")
    private Integer departId;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("部门ids")
    private String[] departIds;
    @ApiModelProperty("部门ids对应的名称集合")
    private String departNames;
    @ApiModelProperty("入岗状态 0待入岗 1已入岗")
    private Integer enterStatus;
    @ApiModelProperty("入职时间")
    private String entryTime;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("民族code")
    private Integer nationCode;
    @ApiModelProperty("民族")
    private String nationCodeStr;
    @ApiModelProperty("属性(1:正编,2:外包)")
    private Integer nature;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("转正时间")
    private String positiveTime;
    @ApiModelProperty("岗位id")
    private Integer postId;
    @ApiModelProperty("岗位级别id")
    private Integer postLevelId;
    @ApiModelProperty("岗位级别名称")
    private String postLevelName;
    @ApiModelProperty("岗位名称")
    private String postName;
    @ApiModelProperty("在岗状态")
    private Integer postStatusCode;
    @ApiModelProperty("在岗状态名称")
    private String postStatusCodeStr;
    @ApiModelProperty("入岗时间")
    private String postTime;
    @ApiModelProperty("性别 0:男,1:女")
    private Integer sex;
    @ApiModelProperty("员工工号(用户工号)")
    private String staffNo;
    @ApiModelProperty("职场id(字典表)")
    private Integer workCode;
    @ApiModelProperty("职场")
    private String workCodeStr;
    @ApiModelProperty("工作地点code")
    private Integer workplaceCode;
    @ApiModelProperty("工作地点")
    private String workplaceCodeStr;
    @ApiModelProperty("专业")
    private String major;
    @ApiModelProperty("资质证照")
    private String staffQuaName;
    @ApiModelProperty("户口地址")
    private String registeredAddress;
    @ApiModelProperty("培训状态(0:未完成,1:已完成)")
    private Integer trainingStatus;
    @ApiModelProperty("商品优势")
    private List<String> productAdvanced;
    @ApiModelProperty("病症优势")
    private List<JSONObject> diseaseAdvanced;
    @ApiModelProperty("培训商品历史")
    private List<String> trainProductHistory;

    @ApiModelProperty(value = "省id", name = "provinceId")
    private Integer provinceId;
    @ApiModelProperty(value = "省名", name = "provinceName")
    private String provinceName;
    @ApiModelProperty(value = "市id", name = "cityId")
    private Integer cityId;
    @ApiModelProperty(value = "市名", name = "cityName")
    private String cityName;
    @ApiModelProperty(value = "区/县id", name = "countyId")
    private Integer countyId;
    @ApiModelProperty(value = "区/县名", name = "countyName")
    private String countyName;
    @ApiModelProperty(value = "街道id", name = "streetId")
    private Integer streetId;
    @ApiModelProperty(value = "街道名", name = "streetName")
    private String streetName;
    @ApiModelProperty(value = "户口省id", name = "registeredProvinceId")
    private Integer registeredProvinceId;
    @ApiModelProperty(value = "户口省名", name = "registeredProvinceName")
    private String registeredProvinceName;
    @ApiModelProperty(value = "户口市id", name = "registeredCityId")
    private Integer registeredCityId;
    @ApiModelProperty(value = "户口市名", name = "registeredCityName")
    private  String registeredCityName;
    @ApiModelProperty(value = "户口区/县id", name = "registeredCountyId")
    private Integer registeredCountyId;
    @ApiModelProperty(value = "户口区/县名", name = "registeredCountyName")
    private  String  registeredCountyName;
    @ApiModelProperty(value = "户口街道id", name = "registeredStreetId")
    private Integer registeredStreetId;
    @ApiModelProperty(value = "户口街道名", name = "registeredStreetName")
    private  String registeredStreetName;

}

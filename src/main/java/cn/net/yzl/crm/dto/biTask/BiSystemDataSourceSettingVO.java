package cn.net.yzl.crm.dto.biTask;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BiSystemDataSourceSettingVO {

    private Integer systemType;//系统类型（1：CRM 2：EHR 3：DMC 等）

    private String menuCode;//菜单编号-对应菜单的权限标识编号

    private String systemPath;//报表路径

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;//生效时间（yyyy-MM-dd）

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;//失效时间（yyyy-MM-dd）

    private Integer isEnable;//是否开启（1:启用 0:禁用）
}

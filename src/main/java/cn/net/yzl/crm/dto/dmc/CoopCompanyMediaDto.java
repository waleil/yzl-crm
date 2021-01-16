package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 合作公司-媒介信息
 * </p>
 *
 * @author liuChangFu
 * @since 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CoopCompanyMedia对象", description = "合作公司-媒介信息")
public class CoopCompanyMediaDto {

    /**
     * 业务主键
     */
    private Long busNo;

    @ApiModelProperty(value = "媒介类型 0:电视媒体, 1:广播电台媒体，2：社区媒体，3：户外媒体，4：印刷媒体，5：互联网媒体，6：电商站内流量媒体")
    private Integer mediaType;

    @ApiModelProperty(value = "媒介名称")
    private String mediaName;


}

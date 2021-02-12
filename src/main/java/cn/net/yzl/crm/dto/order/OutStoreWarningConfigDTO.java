package cn.net.yzl.crm.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhouchangsong
 */
@Data
@ApiModel("出库预警配置对象")
public class OutStoreWarningConfigDTO implements Serializable {

    /**  */
	private static final long serialVersionUID = 3549432958087629043L;

	@ApiModelProperty("预警时间")
    @NotNull(message = "预警时间不可为空")
    private Integer delay;

    @ApiModelProperty("预警方式，1短信，2邮件，3短信+邮件")
    @NotNull(message = "预警方式不可为空")
    private Integer noticeType;

}

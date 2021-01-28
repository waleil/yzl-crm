package cn.net.yzl.crm.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * member_action_relation
 *
 * @author
 */
@Data
@ApiModel(value = "MemberServiceJourneryDto", description = "顾客服务旅程实体")
public class MemberServiceJourneryDto implements Serializable {

    @ApiModelProperty(value = "维护坐席的个数", name = "staffNum")
    private int staffNum;
    @ApiModelProperty(value = "顾客服务旅程实体信息集合", name = "memberServiceJourneryList")
    private List<MemberServiceJournery> memberServiceJourneryList;


}


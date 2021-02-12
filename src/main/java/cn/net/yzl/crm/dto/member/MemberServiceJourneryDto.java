package cn.net.yzl.crm.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * member_action_relation
 *
 * @author
 */
@Data
@ApiModel(value = "MemberServiceJourneryDto", description = "顾客服务旅程实体")
public class MemberServiceJourneryDto implements Serializable {

    /**  */
	private static final long serialVersionUID = -9066654290279614355L;
	@ApiModelProperty(value = "维护坐席的个数", name = "staffNum")
    private int staffNum;
    @ApiModelProperty(value = "顾客服务旅程实体信息集合", name = "memberServiceJourneryList")
    private List<MemberServiceJournery> memberServiceJourneryList;


}


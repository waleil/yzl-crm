package cn.net.yzl.crm.dto.member.customerJourney;

import cn.net.yzl.order.model.vo.order.PortraitOrderDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "MemberCustomerJourneyDto", description = "顾客旅程实体")
@Data
public class MemberCustomerJourneyDto implements Serializable {


    @ApiModelProperty("订单信息,核弹信息,订单拒收,追单信息")
    private List<PortraitOrderDetailDTO> portraitOrderDetailList;


}
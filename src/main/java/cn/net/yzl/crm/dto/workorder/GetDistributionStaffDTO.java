package cn.net.yzl.crm.dto.workorder;

import cn.net.yzl.workorder.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetDistributionStaffDTO extends BaseDTO<GetDistributionStaffDTO> {

    private String staffNo;
}

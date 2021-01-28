package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderTranckDetailClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.IOrderTranckDetailService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderTrackDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTranckDetailServiceImpl implements IOrderTranckDetailService {

    @Autowired
    private OrderTranckDetailClient service;

    @Autowired
    private EhrStaffClient ehrStaffClient;
    @Override
    public ComResponse<Boolean> saveOrderTranckDetail(OrderTrackDetailDTO dto) {

        // 按员工号查询员工信息
        ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
            throw new BizException(sresponse.getCode(),sresponse.getMessage());
        }
        dto.setDepartId(sresponse.getData().getDepartId());
        dto.setCreateCode(sresponse.getData().getStaffNo());
        dto.setCreateUserName(sresponse.getData().getName());

        return service.saveOrderTranckDetail(dto);
    }
}

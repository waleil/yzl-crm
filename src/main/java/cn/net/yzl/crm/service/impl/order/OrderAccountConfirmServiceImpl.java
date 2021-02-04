package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OrderAccountConfirmClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderAccountConfirmService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderAccountConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class OrderAccountConfirmServiceImpl implements OrderAccountConfirmService {
    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Resource
    private OrderAccountConfirmClient client;

    @Override
    public ComResponse<Boolean> saveOrderAccountConfirm(OrderAccountConfirmVO vo) {
        ComResponse<StaffImageBaseInfoDto> response = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(response.getCode(),response.getMessage());
        }
        vo.setCreateCode(response.getData().getStaffNo());
        vo.setCreateName(response.getData().getName());
        vo.setDepartId(String.valueOf(response.getData().getDepartId()));

        return client.saveOrderAccountConfirm(vo);
    }
}

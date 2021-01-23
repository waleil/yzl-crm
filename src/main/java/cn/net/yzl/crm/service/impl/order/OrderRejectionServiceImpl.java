package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderRejectionService;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.enums.RedisKeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhouchangsong
 */
@Service
public class OrderRejectionServiceImpl implements OrderRejectionService {

    @Autowired
    private OrderRejectionClient orderRejectionClient;
    @Autowired
    private EhrStaffClient ehrStaffClient;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @param orderRejectionAddDTO
     * @param userNo
     * @return
     */
    @Override
    public ComResponse addOrderRejection(OrderRejectionAddDTO orderRejectionAddDTO, String userNo) {
        Integer departId = 0;
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(userNo);
        if (detailsByNo.getStatus().compareTo(1) == 0) {
            StaffImageBaseInfoDto data = detailsByNo.getData();
            if (Objects.nonNull(data)) {
                departId = data.getDepartId();
            }
        }
//        storeFeginService.selectStore(orderRejectionAddDTO.getStoreNo())
		// 拒收单编号
		String seqNo = redisUtil.getSeqNo(RedisKeys.REJECT_ORDER_NO_PREFIX, String.valueOf(departId), userNo,
				RedisKeys.SALE_ORDER_NO, 4);
        cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO dto = new cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO();
        dto.setOrderNo(orderRejectionAddDTO.getOrderNo());
        dto.setRejectType(orderRejectionAddDTO.getRejectType());
        dto.setDepartId(departId);
        dto.setRejectionNo(seqNo);
        dto.setStoreNo(orderRejectionAddDTO.getStoreNo());
//        dto.setStoreName();
        dto.setUserNo(userNo);
        dto.setUserName(detailsByNo.getData().getName());
        return orderRejectionClient.addOrderRejection(dto);
    }
}

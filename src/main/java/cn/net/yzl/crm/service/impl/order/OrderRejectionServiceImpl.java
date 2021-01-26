package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderRejectionService;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.enums.RedisKeys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhouchangsong
 */
@Service
@Slf4j
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
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(userNo);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(detailsByNo.getCode())) {
            log.error("拒收单-添加拒收单>>找不到该坐席信息>>{}", detailsByNo);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席信息。");
        }
        Integer departId = Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getDepartId).orElse(0);
        ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(departId);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
            log.error("拒收单-添加拒收单>>找不到该坐席的财务归属>>{}", dresponse);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "找不到该坐席的财务归属。");
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
        dto.setStoreNo(String.valueOf(orderRejectionAddDTO.getStoreNo()));
//        dto.setStoreName();
        dto.setUserNo(userNo);
        dto.setFinancialOwner(Long.valueOf(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartId).orElse(0)));
        dto.setFinancialOwnerName(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartName).orElse("无"));
        dto.setUserName(Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getName).orElse(null));
        return orderRejectionClient.addOrderRejection(dto);
    }
}

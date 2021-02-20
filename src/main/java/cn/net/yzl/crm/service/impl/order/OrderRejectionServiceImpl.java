package cn.net.yzl.crm.service.impl.order;

import java.util.Optional;

import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.model.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderRejectionService;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.enums.RedisKeys;
import lombok.extern.slf4j.Slf4j;

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
    @Autowired
    private StoreFeginService storeFeginService;

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
        // 拒收单编号
        String seqNo = redisUtil.getSeqNo(RedisKeys.REJECT_ORDER_NO_PREFIX, RedisKeys.SALE_ORDER_NO, 6);
        cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO dto = new cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO();
        dto.setOrderNo(orderRejectionAddDTO.getOrderNo());
        dto.setRejectType(orderRejectionAddDTO.getRejectType());
        dto.setDepartId(String.valueOf(departId));
        dto.setRejectionNo(seqNo);
        dto.setStoreNo(String.valueOf(orderRejectionAddDTO.getStoreNo()));
        dto.setUserNo(userNo);
        dto.setFinancialOwner(Long.valueOf(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartId).orElse(0)));
        dto.setFinancialOwnerName(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartName).orElse("无"));
        dto.setUserName(Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getName).orElse(null));
        if (orderRejectionAddDTO.getRejectType().compareTo(1) == 0) {
            dto.setStoreNo("");
        } else {
            //仓库信息
            ComResponse<StoreVO> storeResponse = storeFeginService.selectStoreByNo(orderRejectionAddDTO.getStoreNo());
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(storeResponse.getCode())) {
                log.error("拒收单-添加拒收单>>仓库服务异常>>{}", storeResponse);
                return ComResponse.fail(ResponseCodeEnums.ERROR, "仓库服务异常。");
            }
            StoreVO storeVO = storeResponse.getData();
            if (!Optional.ofNullable(storeVO).isPresent()) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "未找到仓库信息。");
            }
            if (!storeVO.getStatus().equals(1)) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "添加失败，仓库未启用。");
            }

        }
        return orderRejectionClient.addOrderRejection(dto);
    }
}

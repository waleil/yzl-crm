package cn.net.yzl.crm.service.impl.order;

import java.util.Optional;

import cn.net.yzl.activity.model.enums.RejectionTypeEnum;
import cn.net.yzl.activity.model.requestModel.RejectionOrderRequest;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.constant.ObtainType;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.model.vo.StoreVO;
import cn.net.yzl.order.enums.PayMode;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
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

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private MemberFien memberFien;

    /**
     * @param orderRejectionAddDTO
     * @param userNo
     * @return
     */
    @Override
    public ComResponse addOrderRejection(OrderRejectionAddDTO orderRejectionAddDTO, String userNo) {
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(userNo);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(detailsByNo.getCode())) {
            log.error("?????????-???????????????>>EHR????????????>>{}", detailsByNo);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "EHR??????????????????????????????"+detailsByNo.getMessage());
        }
        Integer departId = Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getDepartId).orElse(0);
        ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(departId);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
            log.error("?????????-???????????????>>EHR????????????>>{}", dresponse);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "EHR??????????????????????????????"+dresponse.getMessage());
        }
        // ???????????????
        String seqNo = redisUtil.getSeqNo(RedisKeys.REJECT_ORDER_NO_PREFIX, RedisKeys.SALE_ORDER_NO, 6);
        cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO dto = new cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO();
        dto.setOrderNo(orderRejectionAddDTO.getOrderNo());
        dto.setRejectType(orderRejectionAddDTO.getRejectType());
        dto.setDepartId(String.valueOf(departId));
        dto.setRejectionNo(seqNo);
        dto.setStoreNo(String.valueOf(orderRejectionAddDTO.getStoreNo()));
        dto.setUserNo(userNo);
        dto.setFinancialOwner(Long.valueOf(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartId).orElse(0)));
        dto.setFinancialOwnerName(Optional.ofNullable(dresponse.getData()).map(DepartDto::getFinanceDepartName).orElse("???"));
        dto.setUserName(Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getName).orElse(null));
        if (orderRejectionAddDTO.getRejectType().compareTo(1) == 0) {
            dto.setStoreNo("");
        } else {
            //????????????
            ComResponse<StoreVO> storeResponse = storeFeginService.selectStoreByNo(orderRejectionAddDTO.getStoreNo());
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(storeResponse.getCode())) {
                log.error("?????????-???????????????>>??????????????????>>{}", storeResponse);
                return ComResponse.fail(ResponseCodeEnums.ERROR, "????????????????????????????????????"+dresponse.getMessage());
            }
            StoreVO storeVO = storeResponse.getData();
            if (!Optional.ofNullable(storeVO).isPresent()) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "????????????????????????");
            }
            if (!storeVO.getStatus().equals(1)) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "?????????????????????????????????");
            }
            dto.setStoreName(storeVO.getName());
            dto.setStoreNo(storeVO.getNo());

        }
        ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(dto.getOrderNo());
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            log.error("???????????????????????????{}->?????????????????????????????????????????? {}",dto.getOrderNo(),response);
            throw new BizException(response.getCode(),response.getMessage());
        }

        //??????dmc??????????????????????????????
        OrderM orderM = response.getData().getOrder();
        RejectionOrderRequest entity = new RejectionOrderRequest();
        entity.setMemberCard(orderM.getMemberCardNo());
        entity.setOrderNo(orderM.getOrderNo());
        entity.setUserNo(dto.getUserNo());
        entity.setRejectionType(RejectionTypeEnum.REJECTION_ORDER);
        ComResponse comResponse = activityClient.rejectionOrder(entity);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(comResponse.getCode())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "DMC?????????????????????????????????????????????");
        }
        if(orderM.getPayMode() == PayMode.PAY_MODE_4.getCode()){
            //???????????????????????? ??????
            //????????????????????????
            MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
            if(orderM.getAmountStored() >0){
                // ??????????????????????????????
                memberAmountDetail.setDiscountMoney(orderM.getAmountStored());// ??????????????????
                memberAmountDetail.setMemberCard(orderM.getMemberCardNo());// ????????????
                memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// ??????
                memberAmountDetail.setOrderNo(orderM.getOrderNo());// ????????????
                memberAmountDetail.setRemark("??????????????????????????????");// ??????
                // ????????????????????????????????????
                ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
                // ??????????????????????????????
                if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
                    log.error("???????????????,????????????{} ->??????????????????????????????????????????>>{}", dto.getOrderNo(),customerAmountOperation);
                    throw new BizException(customerAmountOperation.getCode(),customerAmountOperation.getMessage());
                }

            }
        }
        return orderRejectionClient.addOrderRejection(dto);
    }
}

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
            log.error("拒收单-添加拒收单>>EHR服务异常>>{}", detailsByNo);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "EHR服务异常。异常信息："+detailsByNo.getMessage());
        }
        Integer departId = Optional.ofNullable(detailsByNo.getData()).map(StaffImageBaseInfoDto::getDepartId).orElse(0);
        ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(departId);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
            log.error("拒收单-添加拒收单>>EHR服务异常>>{}", dresponse);
            return ComResponse.fail(ResponseCodeEnums.ERROR, "EHR服务异常。异常信息："+dresponse.getMessage());
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
                return ComResponse.fail(ResponseCodeEnums.ERROR, "仓库服务异常。异常信息："+dresponse.getMessage());
            }
            StoreVO storeVO = storeResponse.getData();
            if (!Optional.ofNullable(storeVO).isPresent()) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "未找到仓库信息。");
            }
            if (!storeVO.getStatus().equals(1)) {
                return ComResponse.fail(ResponseCodeEnums.ERROR, "添加失败，仓库未启用。");
            }
            dto.setStoreName(storeVO.getName());
            dto.setStoreNo(storeVO.getNo());

        }
        ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(dto.getOrderNo());
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            log.error("新建拒收单，订单号{}->调用订单服务查询订单信息失败 {}",dto.getOrderNo(),response);
            throw new BizException(response.getCode(),response.getMessage());
        }

        //调用dmc接口，释放赠送的优惠
        OrderM orderM = response.getData().getOrder();
        RejectionOrderRequest entity = new RejectionOrderRequest();
        entity.setMemberCard(orderM.getMemberCardNo());
        entity.setOrderNo(orderM.getOrderNo());
        entity.setUserNo(dto.getUserNo());
        entity.setRejectionType(RejectionTypeEnum.REJECTION_ORDER);
        ComResponse comResponse = activityClient.rejectionOrder(entity);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(comResponse.getCode())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "DMC扣除顾客积分，红包，优惠券异常");
        }
        if(orderM.getPayMode() == PayMode.PAY_MODE_4.getCode()){
            //已冻结的账户余额 解冻
            //释放已使用的余额
            MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
            if(orderM.getAmountStored() >0){
                // 组装顾客账户消费参数
                memberAmountDetail.setDiscountMoney(orderM.getAmountStored());// 使用充值金额
                memberAmountDetail.setMemberCard(orderM.getMemberCardNo());// 顾客卡号
                memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 消费
                memberAmountDetail.setOrderNo(orderM.getOrderNo());// 订单编号
                memberAmountDetail.setRemark("拒收，释放冻结的余额");// 备注
                // 调用顾客账户消费服务接口
                ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
                // 如果调用服务接口失败
                if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
                    log.error("新增拒收单,订单号：{} ->调用顾客账户消费服务接口失败>>{}", dto.getOrderNo(),customerAmountOperation);
                    throw new BizException(customerAmountOperation.getCode(),customerAmountOperation.getMessage());
                }

            }
        }
        return orderRejectionClient.addOrderRejection(dto);
    }
}

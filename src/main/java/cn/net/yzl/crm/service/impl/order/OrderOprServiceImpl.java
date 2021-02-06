package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderOprClient;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.client.store.OrderDistributeExpressFeignService;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.constant.ObtainType;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.IOrderCommonService;
import cn.net.yzl.crm.service.order.IOrderOprService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.logistics.enums.OrderStatus;
import cn.net.yzl.model.vo.OrderDistributeExpressVO;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.vo.order.OrderCheckDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
import cn.net.yzl.order.model.vo.order.OrderOprDTO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderOprServiceImpl implements IOrderOprService {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderOprClient orderOprClient;

    @Autowired
    private MemberFien memberFien;
    @Autowired
    private EhrStaffClient ehrStaffClient;
    
    @Autowired
    private OrderDistributeExpressFeignService orderDistributeExpressFeignService;

    @Autowired
    private IOrderCommonService orderCommonService;

    @Override
    public ComResponse<Boolean> cancleOrder(OrderOprDTO dto) {

            // 按员工号查询员工信息
            ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            // 如果服务调用异常
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
                throw new BizException(sresponse.getCode(),sresponse.getMessage());
            }
            dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
            dto.setOprCode(sresponse.getData().getStaffNo());
            dto.setOprName(sresponse.getData().getName());


            //查询订单信息及当前状态
            ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(dto.getOrderNo());
            if(response.getCode().compareTo(Integer.valueOf(200))!=0){
                throw new BizException(response.getCode(),response.getMessage());
            }
            OrderInfoVo vo = response.getData();


            //校验当前订单状态，审批通过后不得取消订单
            if(vo.getOrder().getOrderStatus()==OrderStatus.ORDER_STATUS_8.getCode()){
                throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),"该订单["+dto.getOrderNo()+"]已取消，请勿重复操作！");
            }


            //释放库存
            List<OrderDetail> detailList = vo.getDetails();
            OrderProductVO productVO = new OrderProductVO();
            Map<String, List<OrderDetail>> collect = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getProductCode));
            List<ProductReduceVO> list = new ArrayList<>();
            collect.entrySet().forEach(map ->{
                ProductReduceVO productReduceVO = new ProductReduceVO();
                productReduceVO.setProductCode(map.getValue().get(0).getProductCode());
                productReduceVO.setNum(map.getValue().stream().mapToInt(OrderDetail::getProductCount).sum());
                productReduceVO.setOrderNo(dto.getOrderNo());
                list.add(productReduceVO);

            });
            productVO.setOrderNo(dto.getOrderNo());
            productVO.setProductReduceVOS(list);
            ComResponse<?> stockRes = productClient.increaseStock(productVO);
            if(stockRes.getCode().compareTo(Integer.valueOf(200))!=0){
                throw new BizException(stockRes.getCode(),stockRes.getMessage());
            }

            //释放已使用的余额
             MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
            if(vo.getOrder().getAmountStored() >0){
                // 组装顾客账户消费参数
                memberAmountDetail.setDiscountMoney(vo.getOrder().getAmountStored());// 使用充值金额
                memberAmountDetail.setMemberCard(vo.getOrder().getMemberCardNo());// 顾客卡号
                memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 消费
                memberAmountDetail.setOrderNo(vo.getOrder().getOrderNo());// 订单编号
                memberAmountDetail.setRemark("取消订单，回退已用金额");// 备注
                // 调用顾客账户消费服务接口
                ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
                // 如果调用服务接口失败
                if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
                    log.error("取消订单>>调用顾客账户消费服务接口失败>>{}", customerAmountOperation);
                    this.orderCommonService.insert(productVO, ProductClient.SUFFIX_URL,
                            ProductClient.INCREASE_STOCK_URL, dto.getOprCode(), dto.getOrderNo());
                    throw new BizException(customerAmountOperation.getCode(),customerAmountOperation.getMessage());
                }

            }


            //todo 释放已使用的优惠
            if(vo.getCouponDetail() !=null && vo.getCouponDetail().size()>0){

            }
            //更新订单状态
            ComResponse<?> res = orderOprClient.cancleOrderM(dto.getOrderNo());
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
                log.error("取消订单>>调用订单服务接口失败>>{}", res);
                this.orderCommonService.insert(productVO, ProductClient.SUFFIX_URL,
                        ProductClient.INCREASE_STOCK_URL, dto.getOprCode(), dto.getOrderNo());
                if(vo.getOrder().getAmountStored() >0){
                    memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_2);//
                    this.orderCommonService.insert(memberAmountDetail, MemberFien.CUSTOMER_AMOUNT_OPERATION_URL,
                            MemberFien.CUSTOMER_AMOUNT_OPERATION_URL, dto.getOprCode(), dto.getOrderNo());
                }

                throw new BizException(res.getCode(),res.getMessage());
            }

            return ComResponse.success(true);

    }

    @Override
    public ComResponse<?> checkOrder(OrderCheckDetailDTO dto) {
        // 按员工号查询员工信息
        ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
            throw new BizException(sresponse.getCode(),sresponse.getMessage());
        }
        dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
        dto.setCheckDepartId(sresponse.getData().getDepartId());
        dto.setCheckUserName(sresponse.getData().getName());
        dto.setCheckUserNo(QueryIds.userNo.get());

        //查询订单信息及当前状态
        ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(dto.getOrderNo());
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(response.getCode(),response.getMessage());
        }
        OrderInfoVo vo = response.getData();

        if (null == vo.getOrder()) {
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "订单号:[" + dto.getOrderNo() + "] 不存在！");
        }

        //校验状态
        if(vo.getOrder().getOrderStatus().compareTo(Integer.valueOf(cn.net.yzl.order.enums.OrderStatus.ORDER_STATUS_1.getCode())) != 0 ){
            throw new BizException(ResponseCodeEnums.VALIDATE_ERROR_CODE.getCode(), "订单号:[" + dto.getOrderNo() + "] 已审核，请勿重复提交！");
        }
        if(vo.getOrder().getOrderNature().compareTo(Integer.valueOf(CommonConstant.ORDER_NATURE_T)) == 0) {
            throw new BizException(ResponseCodeEnums.VALIDATE_ERROR_CODE.getCode(), "订单号:[" + dto.getOrderNo() + "] 是免审订单，无需审核！");

        }

        //业务逻辑处理
        if(dto.getCheckStatus() == 0){//审批不通过

            this.disAggreeOrder(dto,vo);

        }else if(dto.getCheckStatus() == 1){//审批通过
            this.aggreeOrder(dto,vo);

        }

        return ComResponse.success(true);
    }

    private void aggreeOrder( OrderCheckDetailDTO dto, OrderInfoVo orderInfoVo) {


        //调用订单服务 1、记录出库预警消息 2、更新订单状态及收货人信息 3、记录审批记录 4、记录操作日志
        ComResponse<?> orderRes = orderOprClient.checkOrder(dto);
        if(orderRes.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(orderRes.getCode(),orderRes.getMessage());
        }

        //todo 给仓库发通知，生成出库单
        List<OrderDistributeExpressVO> list = new ArrayList<>();
        OrderDistributeExpressVO vo= orderCommonService.mkOrderDistributeExpressData(orderInfoVo);


        list.add(vo);


        ComResponse<?> response = orderDistributeExpressFeignService.insertOrderDistributeExpress(list);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(response.getCode())) {
            log.error(response.getClass()+":"+response.getMessage());
            //todo 记录状态，定时任务重新发送
            log.error("发送出库通知失败-》订单号：" +dto.getOrderNo());
        }
    }



    private void disAggreeOrder(OrderCheckDetailDTO dto,OrderInfoVo vo) {
        //释放库存
        List<OrderDetail> detailList = vo.getDetails();
        OrderProductVO productVO = new OrderProductVO();
        Map<String, List<OrderDetail>> collect = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getProductCode));
        List<ProductReduceVO> list = new ArrayList<>();
        collect.entrySet().forEach(map ->{
            ProductReduceVO productReduceVO = new ProductReduceVO();
            productReduceVO.setProductCode(map.getValue().get(0).getProductCode());
            productReduceVO.setNum(map.getValue().stream().mapToInt(OrderDetail::getProductCount).sum());
            productReduceVO.setOrderNo(vo.getOrder().getOrderNo());
            list.add(productReduceVO);

        });
        productVO.setOrderNo(vo.getOrder().getOrderNo());
        productVO.setProductReduceVOS(list);
        ComResponse<?> stockRes = productClient.increaseStock(productVO);
        if(stockRes.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(stockRes.getCode(),stockRes.getMessage());
        }

        //释放已使用的余额
        if(vo.getOrder().getAmountStored() >0){
            // 组装顾客账户消费参数
            MemberAmountDetailVO memberAmountDetail = new MemberAmountDetailVO();
            memberAmountDetail.setDiscountMoney(vo.getOrder().getAmountStored());// 使用充值金额
            memberAmountDetail.setMemberCard(vo.getOrder().getMemberCardNo());// 顾客卡号
            memberAmountDetail.setObtainType(ObtainType.OBTAIN_TYPE_1);// 消费
            memberAmountDetail.setOrderNo(vo.getOrder().getOrderNo());// 订单编号
            memberAmountDetail.setRemark("取消订单，回退已用金额");// 备注
            // 调用顾客账户消费服务接口
            ComResponse<?> customerAmountOperation = this.memberFien.customerAmountOperation(memberAmountDetail);
            // 如果调用服务接口失败
            if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(customerAmountOperation.getCode())) {
                log.error("取消订单>>调用顾客账户消费服务接口失败>>{}", customerAmountOperation);
                throw new BizException(customerAmountOperation.getCode(),customerAmountOperation.getMessage());
            }

        }


        //todo 释放已使用的优惠
        if(vo.getCouponDetail() !=null && vo.getCouponDetail().size()>0){

        }

        //调用订单信息更新
        //调用订单服务 1、记录出库预警消息 2、更新订单状态及收货人信息 3、记录审批记录 4、记录操作日志
        ComResponse<?> orderRes = orderOprClient.checkOrder(dto);
        if(orderRes.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(orderRes.getCode(),orderRes.getMessage());
        }

    }
}

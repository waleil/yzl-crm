package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.BeanUtil;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.service.order.IOrderCommonService;
import cn.net.yzl.crm.utils.BeanCopyUtils;
import cn.net.yzl.model.vo.OrderDistributeExpressVO;
import cn.net.yzl.model.vo.OrderProductVo;
import cn.net.yzl.order.model.db.order.OrderCouponDetail;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
import cn.net.yzl.order.model.vo.order.OrderProductDTO;
import cn.net.yzl.order.util.MathUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class OrderCommonServiceImpl implements IOrderCommonService {
    
    
    @Override
    public OrderDistributeExpressVO mkOrderDistributeExpressData(OrderInfoVo orderInfoVo) {

        OrderM order = orderInfoVo.getOrder();
        OrderDistributeExpressVO vo = new OrderDistributeExpressVO();
        vo.setOutStoreNo(orderInfoVo.getDetails().get(0).getStoreNo());
        vo.setOrderNo(order.getOrderNo());
        vo.setMediaType(order.getMediaType().toString());
        vo.setMediaName(order.getMediaName());
        vo.setMemberName(order.getMemberName());
        vo.setMemberPhone(order.getMemberTelphoneNo());
        vo.setMemberNum(order.getMemberCardNo());
        vo.setPayType(order.getPayType());
        vo.setTotalAll(order.getTotalAll());
        vo.setCash(order.getCash());
        vo.setOrderTime(order.getCreateTime());
        vo.setTargetProvince(order.getReveiverProvince());
        vo.setTargetCity(order.getReveiverCityName());
        vo.setTargetArea(order.getReveiverAreaName());
        vo.setAddr(order.getReveiverAddress());
        vo.setFinancialOwnerId(order.getFinancialOwner());
        vo.setFinancialOwner(order.getFinancialOwnerName());
        vo.setRemark(order.getRemark());
        vo.setStoreNo(vo.getOutStoreNo());
        vo.setStaffNo(order.getStaffCode());
        vo.setOrderOfficial(order.getTotal());
        vo.setOrderRebate(order.getReturnAmountCoupon());
        vo.setOrderIntegral(order.getPointsDeduction());
        vo.setOrderUseSpare(order.getAmountStored());
        vo.setOrderUseRed(order.getAmountRedEnvelope());
        vo.setOrderUseRebate(order.getAmountCoupon());
        vo.setOrderUseIntegral(order.getPointsDeduction());
        vo.setOrderAfterSpare(order.getOrderAfterSpare());
        vo.setOrderAfterRed(order.getOrderAfterRed());
        vo.setOrderAfterRebate(order.getOrderAfterRebate());
        vo.setOrderAfterIntegral(order.getOrderAfterIntegral());
        vo.setCreateUser(order.getStaffCode());
        List<OrderProductVo> list = mkOrderDetailList(orderInfoVo);

        vo.setOrderProductVoList(list);
        return vo;
    }

    private List<OrderProductVo> mkOrderDetailList(OrderInfoVo vo) {

        List<OrderDetail> orderDetails = vo.getDetails();
        List<OrderDetailDTO> list = BeanCopyUtils.transferList(orderDetails,OrderDetailDTO.class);
        List<OrderCouponDetail> couponDetails = vo.getCouponDetail();
        if(couponDetails != null && couponDetails.size()>0) {
            list.forEach(map -> {
                couponDetails.forEach(item -> {
                    if (map.getOrderDetailCode().equals(item.getOrderDetailCode())) {
                        if (item.getCouponType() == 2) {
                            map.setAmountRedEnvelope(item.getCouponAmt());
                        } else if (item.getCouponType() == 1) {
                            map.setAmountCoupon(item.getCouponAmt());
                        } else if (item.getCouponType() == 4) {
                            map.setPointsDeduction(item.getCouponAmt());
                        }
                    }
                });
            });
        }
        List<OrderProductVo> orderProductVos = new ArrayList<>();
        list.forEach(map->{
            OrderProductVo orderProductVo = new OrderProductVo();
            orderProductVo.setOutStoreNo(String.valueOf(map.getStoreNo()));//仓库号
            orderProductVo.setOrderNo(map.getOrderNo());//订单号
            orderProductVo.setProductCode(map.getProductCode());//商品code
            orderProductVo.setProductName(map.getProductName());//产品名称
            orderProductVo.setUnit(map.getUnit());//产品规格
            orderProductVo.setNum(map.getProductCount());//产品数量
            orderProductVo.setPrice(map.getTotal());//产品金额
            orderProductVo.setUseRebate(map.getAmountCoupon());//优惠券扣减
            orderProductVo.setUseSpareMoney(map.getPointsDeduction());//积分扣减
            orderProductVo.setUseRedPacket(map.getAmountRedEnvelope());//红包扣减
            orderProductVo.setCash(map.getCash());//实收金额
            orderProductVos.add(orderProductVo);


        });

        return orderProductVos;

    }
}

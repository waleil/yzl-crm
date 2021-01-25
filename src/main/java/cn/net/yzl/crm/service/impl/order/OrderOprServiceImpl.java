package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderOprClient;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.controller.order.OrderOprController;
import cn.net.yzl.crm.service.order.IOrderOprService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.logistics.enums.OrderStatus;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderOprServiceImpl implements IOrderOprService {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderOprClient orderOprClient;



    @Override
    public ComResponse<Boolean> cancleOrder(String orderNo) {


        //查询订单信息及当前状态
        ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(orderNo);
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(response.getCode(),response.getMessage());
        }
        OrderInfoVo vo = response.getData();
        //校验当前订单状态，审批通过后不得取消订单
        if(vo.getOrder().getOrderStatus()==OrderStatus.ORDER_STATUS_8.getCode()){
            throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),"该订单["+orderNo+"]已取消，请勿重复操作！");
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
            productReduceVO.setOrderNo(orderNo);
            list.add(productReduceVO);

        });
        productVO.setOrderNo(orderNo);
        productVO.setProductReduceVOS(list);
        ComResponse stockRes = productClient.increaseStock(productVO);
        if(stockRes.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(stockRes.getCode(),stockRes.getMessage());
        }
        // todo 释放已使用的余额
        if(vo.getOrder().getAmountStored() >0){

        }


        //todo 释放已使用的优惠
        if(vo.getCouponDetail() !=null && vo.getCouponDetail().size()>0){

        }
        //更新订单状态
        ComResponse res = orderOprClient.cancleOrderM(orderNo);

        return ComResponse.success(true);
    }
}

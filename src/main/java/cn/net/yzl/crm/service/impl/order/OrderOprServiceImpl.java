package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.service.order.IOrderOprService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.vo.order.OrderDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderOprServiceImpl implements IOrderOprService {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private ProductClient productClient;

    @Override
    public ComResponse<Integer> cancleOrder(String orderNo) {
        //查询订单信息及当前状态
        ComResponse<OrderInfoVo> response = orderSearchClient.selectOrderInfo4Opr(orderNo);
        if(response.getCode().compareTo(Integer.valueOf(200))!=0){
            throw new BizException(response.getCode(),response.getMessage());
        }
        OrderInfoVo vo = response.getData();
        //释放库存
        List<OrderDetail> detailList = vo.getDetails();
        //释放已使用的余额
        OrderProductVO productVO = new OrderProductVO();
        Map<String, List<OrderDetail>> collect = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getProductCode));
        collect.entrySet().forEach(map ->{

        });


        //释放已使用的优惠

        //更新订单状态
        return null;
    }
}

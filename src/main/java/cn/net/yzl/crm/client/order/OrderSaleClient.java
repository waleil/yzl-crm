package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleDTO;
import cn.net.yzl.order.model.vo.order.OrderSaleVO;
import cn.net.yzl.order.model.vo.order.UpdateOrderCheckSettingDTO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "orderSale",url = "${api.gateway.url}/orderService/orderSale")
public interface OrderSaleClient {
    /**
     * 新建或者修改售后订单
     * @return
     */
    @PostMapping(path="v1/saveOrUpdateOrderSale")
    public ComResponse saveOrUpdateOrderSale(@RequestBody @Validated OrderSale orderSalem);

    /**
     * 查询售后单详情
     * @return
     */
    @GetMapping(path="v1/selectOrderSaleList")
    public ComResponse selectOrderSaleList(@RequestParam(required = false) String orderNo,
                                           @RequestParam(required = false) Integer saleOrderType,
                                           @RequestParam(required = false) Integer refundType,
                                           @RequestParam(required = false) String memberName,
                                           @RequestParam(required = false) String createStartTime,
                                           @RequestParam(required = false) String createEndTime);

    /**
     * 查询售后单详情
     * @return
     */
    @GetMapping(path="v1/selectOrderSaleInfo")
    public ComResponse selectOrderSaleInfo(@RequestParam(required = false) String orderNo, @RequestParam String saleOrderNo);
}

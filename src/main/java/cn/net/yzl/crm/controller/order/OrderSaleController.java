package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSaleClient;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleDTO;
import cn.net.yzl.order.model.vo.order.OrderSaleVO;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("orderSale")
@Api(tags = "售后订单管理")
public class OrderSaleController {
    @Autowired
   private OrderSaleClient orderSaleClient;

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "新建或修改售后订单")
    @PostMapping("v1/saveOrUpdateOrderSale")
        public ComResponse saveOrUpdateOrderSale(@RequestBody @Validated OrderSale orderSalem, HttpServletRequest request) {
        ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
        if (userNo.getData()!=null){
            String workCodeStr = userNo.getData().getWorkCodeStr();
            orderSalem.setSaleOrderNo(workCodeStr);
        }
        ComResponse comResponse =null;
        if (orderSalem.getSaleOrderNo()==null){
            orderSalem.setCreateCode(request.getHeader("userNo"));
            comResponse = orderSaleClient.saveOrderSale(orderSalem);
        }else {
            orderSalem.setUpdateCode(request.getHeader("userNo"));
            comResponse = orderSaleClient.updateOrderSale(orderSalem);
        }
        return comResponse;
    }

    @ApiOperation(value = "查询售后单列表")
    @GetMapping("v1/selectOrderSaleList")
    public ComResponse selectOrderSaleList(@RequestParam(required = false) String orderNo,
                                              @RequestParam(required = false) Integer saleOrderType,
                                              @RequestParam(required = false) Integer refundType,
                                              @RequestParam(required = false) String memberName,
                                              @RequestParam(required = false) String createStartTime,
                                              @RequestParam(required = false) String createEndTime) {

        ComResponse comResponse = orderSaleClient.selectOrderSaleList(orderNo,saleOrderType,refundType,memberName,createStartTime,createEndTime);
        return comResponse;
    }

    @ApiOperation(value = "查询售后单详情")
    @GetMapping("v1/selectOrderSaleInfo")
    public ComResponse<?> selectOrderSaleInfo(@RequestParam(required = false) String orderNo,
                                              @RequestParam String saleOrderNo) {
        ComResponse comResponse = orderSaleClient.selectOrderSaleInfo(orderNo, saleOrderNo);
        return comResponse;
    }

}

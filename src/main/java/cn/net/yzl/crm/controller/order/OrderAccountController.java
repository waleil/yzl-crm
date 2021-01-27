package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderAccountClient;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.service.order.OrderRejectionService;

import cn.net.yzl.order.model.vo.order.OrderListAccuntDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author cuileilei
 */
@RestController
@Slf4j
@RequestMapping("orderAccount")
@Api(tags = "结算中心退款管理")
public class OrderAccountController {
    @Autowired
    private OrderAccountClient orderAccountClient;


    @GetMapping("v1/getOrderAccountList")
    @ApiOperation(value = "查询退款订单分页")
    public ComResponse<Page<OrderListAccuntDTO>> getOrderRejectionList(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo,
                                                                       @ApiParam(value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                       @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                       @ApiParam(value = "财务归属") @RequestParam("financialOwnerName") String financialOwnerName,
                                                                       @ApiParam(value = "顾客姓名或会员卡号") @RequestParam("nameOrCard") String nameOrCard,
                                                                       @ApiParam(value = "开始时间") @RequestParam("startTime") String startTime,
                                                                       @ApiParam(value = "结束时间") @RequestParam("endTime") String endTime) {
        return orderAccountClient.getOrderAccountList(orderNo, pageNo, pageSize,nameOrCard,financialOwnerName,startTime,endTime);
    }

}

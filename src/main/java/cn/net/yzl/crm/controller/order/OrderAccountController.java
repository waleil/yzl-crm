package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderAccountClient;

import cn.net.yzl.crm.client.order.OrderFinanceClient;
import cn.net.yzl.order.model.db.order.OrderFinance;
import cn.net.yzl.order.model.vo.order.OrderAccoundInfoDTO;
import cn.net.yzl.order.model.vo.order.OrderListAccuntDTO;
import cn.net.yzl.order.model.vo.order.PaymentInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @Autowired
    private OrderFinanceClient orderFinanceClient;


    @GetMapping("v1/getOrderFinanceList")
    @ApiOperation(value = "财务归属接口")
    public ComResponse<List<OrderFinance>> getOrderFinanceList() {
        return orderFinanceClient.getOrderFinanceList();
    }

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

    @ApiOperation(value = "退款管理查看订单基本信息")
    @GetMapping("v1/selectAccountOrderInfo")
    public ComResponse<OrderAccoundInfoDTO> selectOrderInfo(@RequestParam("saleOrderNo")
                                                            @NotEmpty(message = "售后申请单编号不能为空")
                                                            @ApiParam(value="售后申请单编号",required=true)String saleOrderNo,
                                                            @RequestParam("orderNo")
                                                            @NotEmpty(message = "售后申请单编号不能为空")
                                                            @ApiParam(value="售后申请单编号",required=true)String orderNo) {



        return orderAccountClient.selectAccountOrderInfo(saleOrderNo,orderNo);
    }

    @ApiOperation(value = "保存退款信息")
    @PostMapping("v1/saveAccountOrderInfo")
    public ComResponse<Boolean> saveOrderInfo(@RequestBody @Valid PaymentInfoDTO dto) {
        ComResponse comResponse = null;
        try {
            comResponse = orderAccountClient.saveAccountOrderInfo(dto);
        } catch (Exception e) {
            // 远程调用 异常
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }


        return comResponse;
    }

}

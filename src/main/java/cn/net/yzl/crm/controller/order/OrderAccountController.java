package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderAccountClient;

import cn.net.yzl.order.model.vo.order.OrderAccoundInfoDTO;
import cn.net.yzl.order.model.vo.order.OrderListAccuntDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    public ComResponse<Boolean> saveOrderInfo(@RequestParam("accPayeeAccount")
                                                  @NotEmpty(message = "退款账号不能为空")
                                                  @ApiParam(value = "退款账号", required = true) String accPayeeAccount,
                                              @RequestParam("transactionNo")
                                                  @NotEmpty(message = "交易流水号不能为空")
                                                  @ApiParam(value = "交易流水号", required = true) String transactionNo,
                                              @RequestParam("orderAccountTime")
                                                  @NotEmpty(message = "退款时间")
                                                  @ApiParam(value = "退款时间", required = true) String orderAccountTime,
                                              @RequestParam("saleOrderNo")
                                                  @NotEmpty(message = "售后申请单编号不能为空")
                                                  @ApiParam(value = "售后申请单编号", required = true) String saleOrderNo,
                                              @RequestParam("updateCode")
                                                  @NotEmpty(message = "修改人员工编号不能为空")
                                                  @ApiParam(value = "修改人员工编号", required = true) String updateCode,
                                              @RequestParam("updateName")
                                                  @NotEmpty(message = "修改人员工姓名不能为空")
                                                  @ApiParam(value = "修改人员工姓名", required = true) String updateName,
                                              @RequestParam("isn")
                                                  @NotEmpty(message = "退款标识不能为空")
                                                  @ApiParam(value = "退款表示：0=全部退款，1=部分退款", required = true) String isn) {
        ComResponse comResponse = null;
        try {
            comResponse = orderAccountClient.saveAccountOrderInfo(accPayeeAccount,transactionNo,orderAccountTime,saleOrderNo,updateCode,updateName,isn);
        } catch (Exception e) {
            // 远程调用 异常
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }


        return comResponse;
    }

}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderAccountClient;

import cn.net.yzl.crm.client.order.OrderFinanceClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.db.order.OrderFinance;
import cn.net.yzl.order.model.vo.order.OrderAccoundInfoDTO;
import cn.net.yzl.order.model.vo.order.OrderListAccuntDTO;
import cn.net.yzl.order.model.vo.order.OrderRefundSearchDTO;
import cn.net.yzl.order.model.vo.order.PaymentInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author cuileilei
 */
@RestController
@RequestMapping("orderAccount")
@Api(tags = "结算中心")
public class OrderAccountController {
    @Autowired
    private OrderAccountClient orderAccountClient;
    @Autowired
    private OrderFinanceClient orderFinanceClient;
    @Autowired
    private EhrStaffClient ehrStaffClient;

    @GetMapping("v1/getOrderFinanceList")
    @ApiOperation(value = "财务归属接口")
    public ComResponse<List<OrderFinance>> getOrderFinanceList() {
        return orderFinanceClient.getOrderFinanceList();
    }

    @PostMapping("v1/getOrderAccountList")
    @ApiOperation(value = "查询退款订单分页")
    public ComResponse<Page<OrderListAccuntDTO>> getOrderRejectionList(@RequestBody OrderRefundSearchDTO orderRefundSearchDTO) {
        return orderAccountClient.getOrderAccountList(orderRefundSearchDTO);
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

        // 按员工号查询员工信息
        ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
            throw new BizException(sresponse.getCode(),"调用ehr查询操作人信息失败>>"+sresponse.getMessage());
        }
        dto.setDepartId(String.valueOf(sresponse.getData().getDepartId()));
        dto.setUpdateCode(sresponse.getData().getStaffNo());
        dto.setUpdateName(sresponse.getData().getName());
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

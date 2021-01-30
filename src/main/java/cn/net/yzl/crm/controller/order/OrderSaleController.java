package cn.net.yzl.crm.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.enums.RedisKeys;
import cn.net.yzl.order.model.vo.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderSaleClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/orderSale")
@Api(tags = "售后订单管理")
public class OrderSaleController {
    @Autowired
    private OrderSaleClient orderSaleClient;

    @Autowired
    private EhrStaffClient ehrStaffClient;
    @Autowired
    private MemberFien memberFien;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "新建售后单——（测试中）")
    @PostMapping("/v1/saveOrderSale")
    public ComResponse<Boolean> saveOrUpdateOrderSale(@RequestBody @Validated OrderSaleAddDTO dto,
                                                      HttpServletRequest request) {
        ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
        if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
        }
        StaffImageBaseInfoDto data = userNo.getData();
        String seqNo = redisUtil.getSeqNo(RedisKeys.SALE_ORDER_NO_PREFIX, data.getWorkCode(), data.getStaffNo(),
                RedisKeys.SALE_ORDER_NO, 4);
        dto.setSaleOrderNo(seqNo);
        dto.setCreateCode(data.getStaffNo());
        dto.setCreateName(data.getName());
        dto.setDepartId(data.getDepartId());
        return orderSaleClient.saveOrderSale(dto);
    }

    @ApiOperation(value = "查询售后单列表——（可联调）")
    @GetMapping("/v1/selectOrderSaleList")
    public ComResponse<Page<OrderSaleListVO>> selectOrderSaleList(
            @RequestParam(required = false) @ApiParam(value = "订单编号") String orderNo,
            @RequestParam(required = false) @ApiParam(value = "售后方式：0=退货，1=换货 2=拒收") Integer saleOrderType,
            @RequestParam(required = false) @ApiParam(value = "退款方式：0=快递代办，1=微信转账，2=支付宝转账，3=银行卡转账，4=退回账户余款") Integer refundType,
            @RequestParam(required = false) @ApiParam(value = "顾客名称") String memberName,
            @RequestParam(required = false) @ApiParam(value = "开始时间") String createStartTime,
            @RequestParam(required = false) @ApiParam(value = "结束时间") String createEndTime,
            @RequestParam @ApiParam(value = "每页条数", name = "pageSize") Integer pageSize,
            @RequestParam @ApiParam(value = "当前页码", name = "pageNo") Integer pageNo) {

        return orderSaleClient.selectOrderSaleList(orderNo, saleOrderType, refundType, memberName, createStartTime,
                createEndTime, pageSize, pageNo);
    }

    @ApiOperation(value = "查询售后订单审核页详情")
    @GetMapping("/v1/selectOrderSaleCheckInfo")
    public ComResponse<OrderSaleCheckDetailVO> selectOrderSaleCheckInfo(
            @RequestParam @NotBlank(message = "售后订单不能为空") @ApiParam(value = "售后单号") String saleOrderNo) {
        ComResponse<OrderSaleCheckDetailVO> comResponse = orderSaleClient.selectOrderSaleCheckInfo(saleOrderNo);
        if (comResponse.getData() != null) {
            OrderSaleCheckDetailVO orderSaleCheckDetailVO = comResponse.getData();
            GeneralResult<Member> memberResult = memberFien.getMember(orderSaleCheckDetailVO.getMemberCardNo());
            if (memberResult.getData() != null) {
                Member member = memberResult.getData();
                OrderSaleMemberInfo orderSaleMemberInfo = orderSaleCheckDetailVO.getOrderSaleMemberInfo();
                orderSaleMemberInfo.setMemberProvinceCode(member.getProvince_code());
                orderSaleMemberInfo.setMemberCityCode(member.getCity_code());
                orderSaleMemberInfo.setMemberRegionCode(member.getRegion_code());
                orderSaleMemberInfo.setMemberEmail(member.getEmail());
                orderSaleMemberInfo.setMemberBalance(member.getMember_amount().getTotalMoney());
                orderSaleMemberInfo.setMemberRedPacket(member.getMember_amount().getLastRedBag());
                orderSaleMemberInfo.setMemberCoupon(member.getMember_amount().getLastCoupon());
                orderSaleCheckDetailVO.setOrderSaleMemberInfo(orderSaleMemberInfo);
                comResponse.setData(orderSaleCheckDetailVO);
            }
        }
        return comResponse;
    }

    @ApiOperation(value = "修改页查询订单信息")
    @GetMapping("/v1/selectOrderSaleInfo")
    public ComResponse<OrderSaleDetailVO> selectOrderSaleInfo(
            @RequestParam(required = false) @ApiParam(value = "订单编号") String orderNo,
            @RequestParam @NotBlank(message = "售后单号不能为空") @ApiParam(value = "售后单号", required = true) String saleOrderNo) {
        ComResponse<OrderSaleDetailVO> comResponse = orderSaleClient.selectOrderSaleInfo(orderNo, saleOrderNo);
        if (comResponse.getData() != null) {
            OrderSaleDetailVO orderSaleCheckDetailVO = comResponse.getData();
            GeneralResult<Member> memberResult = memberFien.getMember(orderSaleCheckDetailVO.getMemberCardNo());
            if (memberResult.getData() != null) {
                Member member = memberResult.getData();
                OrderSaleMemberInfo orderSaleMemberInfo = orderSaleCheckDetailVO.getOrderSaleMemberInfo();
                orderSaleMemberInfo.setMemberProvinceCode(member.getProvince_code());
                orderSaleMemberInfo.setMemberCityCode(member.getCity_code());
                orderSaleMemberInfo.setMemberRegionCode(member.getRegion_code());
                orderSaleMemberInfo.setMemberEmail(member.getEmail());
                orderSaleCheckDetailVO.setOrderSaleMemberInfo(orderSaleMemberInfo);
                comResponse.setData(orderSaleCheckDetailVO);
            }
        }
        return comResponse;
    }

    @ApiOperation(value = "售后单审批")
    @GetMapping("/v1/updateOrderSaleState")
    public ComResponse<Boolean> updateOrderSaleState(
            @RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(value = "售后单号", required = true) String orderSaleNo,
            @RequestParam @NotBlank(message = "审批状态不能为空") @ApiParam(value = "审批状态 0:不通过,其他:通过", required = true) Integer state,
            @RequestParam(required = false) @ApiParam(value = "用户ID") String userNo,
            @RequestParam(required = false) @ApiParam(value = "用户岗位") Integer userWorkInfo,
            @RequestParam(required = false) @ApiParam(value = "备注") String remark,
            @RequestParam(required = false) @ApiParam(value = "售后单方式：0=退货，1=换货 2=拒收") String checkType) {
        return orderSaleClient.updateOrderSaleState(orderSaleNo, state, userNo, userWorkInfo, remark, checkType);
    }

    @ApiOperation(value = "查询售后订单审批列表")
    @GetMapping("/v1/selectOrderSaleCheckList")
    public ComResponse<Page<OrderSaleCheckListVO>> selectOrderSaleCheckList(
            @RequestParam(required = false) @ApiParam(value = "订单编号") String orderNo,
            @RequestParam(required = false) @ApiParam(value = "顾客姓名") String memberName,
            @RequestParam(required = false) @ApiParam(value = "开始时间") String createStartTime,
            @RequestParam(required = false) @ApiParam(value = "结束时间") String createEndTime,
            @RequestParam(required = false, defaultValue = "18") @ApiParam(value = "页数") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") @ApiParam(value = "条数") Integer pageNo,
            @RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(value = "售后单状态 1:未审核,其他:已审核", required = true) Integer state) {
        return orderSaleClient.selectOrderSaleCheckList(orderNo, memberName, createStartTime, createEndTime, pageSize,
                pageNo, state);
    }

    @ApiOperation(value = "根据订单号查询订单信息——（可联调）")
    @GetMapping("/v1/selectOrderSaleProductInfoByOrderNo")
    public ComResponse<CreateOrderSaleForSearchDTO> selectOrderSaleProductInfoByOrderNo(
            @RequestParam @ApiParam(value = "订单编号") String orderNo) {
        return orderSaleClient.selectOrderSaleProductInfoByOrderNo(orderNo);
    }
}
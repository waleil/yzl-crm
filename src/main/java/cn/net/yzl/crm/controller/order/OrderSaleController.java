package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.member.MemberClient;
import cn.net.yzl.crm.client.order.OrderSaleClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.model.vo.order.NewCreateOrderSaleVO;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleDTO;
import cn.net.yzl.order.model.vo.order.OrderSaleVO;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
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
    private EhrStaffClient ehrStaffClient
            ;
    @Autowired
    private MemberFien memberFien;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "新建或修改售后订单")
    @PostMapping("v1/saveOrUpdateOrderSale")
        public ComResponse<Boolean> saveOrUpdateOrderSale(@RequestBody @Validated OrderSale orderSalem, HttpServletRequest request) {
        ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
        if (userNo.getData()!=null){
            String workCodeStr = userNo.getData().getWorkCodeStr();
            orderSalem.setSaleOrderNo(workCodeStr);
        }
        ComResponse<Boolean> comResponse =null;
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
    public ComResponse<List<OrderSaleVO>> selectOrderSaleList(@RequestParam(required = false) String orderNo,
                                                 @RequestParam(required = false) Integer saleOrderType,
                                                 @RequestParam(required = false) Integer refundType,
                                                 @RequestParam(required = false) String memberName,
                                                 @RequestParam(required = false) String createStartTime,
                                                 @RequestParam(required = false) String createEndTime) {

        ComResponse<List<OrderSaleVO>> comResponse = orderSaleClient.selectOrderSaleList(orderNo,saleOrderType,refundType,memberName,createStartTime,createEndTime);
        return comResponse;
    }

    @ApiOperation(value = "查询售后单详情")
    @GetMapping("v1/selectOrderSaleInfo")
    public ComResponse<OrderSaleVO> selectOrderSaleInfo(@RequestParam(required = false) String orderNo,
                                              @RequestParam String saleOrderNo) {
        ComResponse<OrderSaleVO> comResponse = orderSaleClient.selectOrderSaleInfo(orderNo, saleOrderNo);
        if (comResponse.getData() !=null){
            OrderSaleVO orderSaleVO = comResponse.getData();
            GeneralResult<Member> memberResult = memberFien.getMember(orderSaleVO.getMemberCardNo());
            if (memberResult.getData()!=null){
                Member member = memberResult.getData();
                orderSaleVO.setMemberProvinceCode(member.getProvince_code());
                orderSaleVO.setMemberCityCode(member.getCity_code());
                orderSaleVO.setMemberRegionCode(member.getRegion_code());
                orderSaleVO.setMemberEmail(member.getEmail());
                comResponse.setData(orderSaleVO);
            }
        }
        return comResponse;
    }
    @ApiOperation(value = "售后单审批")
    @GetMapping("v1/updateOrderSaleState")
    public ComResponse<Boolean> updateOrderSaleState(@RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name="orderSaleNo",value="售后单号",required=true) String orderSaleNo,
                                                     @RequestParam @NotBlank(message = "审批状态不能为空") @ApiParam(name="state",value="审批状态 0:不通过,其他:通过",required=true) Integer checkStatus,
                                                     @RequestParam(required = false) String userNo,
                                                     @RequestParam(required = false) Integer userWorkInfo,
                                                     @RequestParam(required = false) String remark,
                                                     @RequestParam(required = false) String checkType){
        ComResponse<Boolean> booleanComResponse = orderSaleClient.updateOrderSaleState(orderSaleNo, checkStatus,userNo,userWorkInfo,remark,checkType);
        return booleanComResponse;
    }
    @ApiOperation(value = "查询售后单审核列表")
    @GetMapping("v1/selectOrderSaleCheckList")
    public ComResponse<List<OrderSaleVO>> selectOrderSaleCheckList(@RequestParam(required = false) String orderNo,  @RequestParam(required = false) String memberName, @RequestParam(required = false) String createStartTime,@RequestParam(required = false) String createEndTime,@RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name="state",value="售后单状态 1:未审核,其他:已审核",required=true) Integer state){
        ComResponse<List<OrderSaleVO>> listComResponse = orderSaleClient.selectOrderSaleCheckList(orderNo, memberName, createStartTime, createEndTime, state);
        return listComResponse;
    }
    @ApiOperation(value = "查询售后单审核页详情")
    @GetMapping("v1/selectOrderSaleCheckInfo")
    public ComResponse<OrderSaleVO> selectOrderSaleCheckInfo(@RequestParam(required = false) String orderNo,
                                                             @RequestParam @NotBlank(message = "售后单号不能为空") @ApiParam(name="saleOrderNo",value="售后单号",required=true)String saleOrderNo) {
      ComResponse<OrderSaleVO> comResponse = orderSaleClient.selectOrderSaleCheckInfo(orderNo,saleOrderNo);
        return comResponse;

    }
    @ApiOperation(value = "新建售后订单时查询订单信息")
    @GetMapping("v1/selectOrderSaleProductInfo")
    public ComResponse<NewCreateOrderSaleVO> selectOrderSaleProductInfo(@RequestParam(required = false) String orderNo){
        ComResponse<NewCreateOrderSaleVO> comResponse = orderSaleClient.selectOrderSaleProductInfo(orderNo);
        return comResponse;
    }


}
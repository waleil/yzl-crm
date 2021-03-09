package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffChangeRecordDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.model.order.OrderLogistcInfo;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.IOrderSearchService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("orderSearch")
@Api(tags = "订单管理")
public class OrderSeachController {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private IOrderSearchService orderSearchService;


    @Autowired
    private EhrStaffClient ehrStaffClient;

    @ApiOperation(value = "查询订单列表")
    @PostMapping("v1/selectOrderList")
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto) {
        ComResponse<Page<OderListResDTO>> list = orderSearchClient.selectOrderList(dto);
        return list;
    }
    @ApiOperation(value = "根据用户权限查询订单列表")
    @PostMapping("v1/selectOrderList4Right")
    public ComResponse<Page<OderListResDTO>> selectOrderList4Right(@RequestBody OderListReqDTO dto) {
        //当前坐席userNo
        String  userNo = QueryIds.userNo.get();
        // 按员工号查询员工信息
        ComResponse<StaffChangeRecordDto> res = this.ehrStaffClient.getStaffLastChangeRecord(userNo);
        if(res == null){
           throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"调用ehr查询员工信息失败");
       }
        // 如果服务调用异常
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
            log.error("调用【查询员工信息】失败，{} - {}",res.getCode(),res.getMessage());
            throw new BizException(res.getCode(),res.getMessage());
        }
        if(res.getData()==null){
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),"没有查到坐席详情信息");
        }
        String leaderNo = res.getData().getLeaderNo();
        if(userNo.equals(leaderNo)){//说明他是部门领导，可以查看当前部门所有订单
            Integer departId = res.getData().getDepartId();
            ComResponse<List<DepartDto>> departRes = ehrStaffClient.getChildTreeById(departId);
            if(departRes == null){
                throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"调用ehr获取子部门列表失败");
            }
            // 如果服务调用异常
            if (ResponseCodeEnums.NO_DATA_CODE.getCode().equals(departRes.getCode())) {
                //说明没有子部门,则只查询本部门的数据
                dto.setDepartIds(Arrays.asList(departId));

            }else if(ResponseCodeEnums.SUCCESS_CODE.getCode().equals(departRes.getCode())){
                List<Integer> collect = departRes.getData().stream().map(DepartDto::getId).collect(Collectors.toList());
                collect.add(departId);
                dto.setDepartIds(collect);

            }else{
                log.error("调用【获取子部门列表】失败，{} - {}",departRes.getCode(),departRes.getMessage());
                throw new BizException(departRes.getCode(),departRes.getMessage());
            }

        }else{
            dto.setStaffCode(userNo);//员工级，只查询自己的订单
        }
        ComResponse<Page<OderListResDTO>> list = orderSearchClient.selectOrderList(dto);
        return list;
    }


    @ApiOperation(value = "查询订单基本信息")
    @GetMapping("v1/selectOrderInfo")
    public ComResponse<OrderInfoVO> selectOrderInfo(@RequestParam
                                                        @NotNull(message = "订单编号不能为空")
                                                        @ApiParam(value="免审规则类型",required=true)String orderNo) {

        return  orderSearchService.selectOrderInfo(orderNo);
    }


    @ApiOperation(value = "查询订单商品列表")
    @GetMapping("v1/selectOrderProductDetail")
    public  ComResponse<OrderProductListVo> selectOrderProductDetail(@RequestParam
                                                                        @NotNull(message = "订单编号不能为空")
                                                                        @ApiParam(value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderProductDetail(orderNo);
    }

    @ApiOperation(value = "查询订单操作日志")
    @GetMapping("v1/selectOrderLogList")
    public  ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(@RequestParam
                                                                    @NotNull(message = "订单编号不能为空")
                                                                    @ApiParam(value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderLogList(orderNo);
    }
    @ApiOperation(value = "查询订单审核列表")
    @PostMapping("v1/selectOrderList4Check")
    public  ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto) {

        return  orderSearchClient.selectOrderList4Check(dto);
    }

    @ApiOperation(value = "查询物流信息")
    @GetMapping("v1/selectLogisticInfo")
    public  ComResponse<OrderLogistcInfo> selectLogisticInfo(@RequestParam("orderNo") @NotEmpty(message = "订单号不能为空") String orderNo,
                                                             @RequestParam(name = "companyCode" ,required = false)  String companyCode,
                                                             @RequestParam(name = "expressNo",required = false) String expressNo) {

        return  orderSearchService.selectLogisticInfo(orderNo,companyCode,expressNo);
    }

    @ApiOperation(value = "查询订单销售明细")
    @PostMapping("v1/selectOrderSaleDetail")
    public ComResponse<Page<OrderSellDetailResDTO>> selectOrderSaleDetail(@RequestBody OrderSellDetailReqDTO dto) {

        return orderSearchClient.selectOrderSaleDetail(dto);
    }
}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;
import cn.net.yzl.crm.service.order.OrderRejectionService;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author zhouchangsong
 */
@RestController
@RequestMapping(value = "orderRejection")
@Api(tags = "拒收单控制层")
public class OrderRejectionController {

    @Autowired
    private OrderRejectionClient orderRejectionClient;
    @Autowired
    private OrderRejectionService orderRejectionService;


    @GetMapping("v1/getOrderRejectionList")
    @ApiOperation(value = "查询拒收订单分页")
    public ComResponse<Page<OrderRejectionPageDTO>> getOrderRejectionList(
            @ApiParam(value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return orderRejectionClient.getOrderRejectionList("", pageNo, pageSize);
    }

    @GetMapping("v1/getOrderList")
    @ApiOperation(value = "查询待发货订单")
    public ComResponse<Page<OderListResDTO>> selectOrderList(@ApiParam(value = "订单号") @RequestParam String orderNo,
                                                             @ApiParam(value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                             @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return orderRejectionClient.selectOrderList(pageNo, pageSize, orderNo);
    }

    /**
     * 查询拒收单详情
     *
     * @param orderNo
     * @return
     */
    @GetMapping("v1/getOrderRejectionDetail")
    @ApiOperation(value = "查询拒收单详情")
    public ComResponse<OrderRejectionDetailDTO> getOrderRejectionDetail(@ApiParam(value = "订单号") @RequestParam String orderNo) {
        return orderRejectionClient.getOrderRejectionDetail(orderNo);
    }

    @PostMapping("v1/addOrderRejection")
    @ApiOperation(value = "新增拒收订单")
    public ComResponse addOrderRejection( @Valid @RequestBody OrderRejectionAddDTO orderRejectionAddDTO) {
        String userNo = QueryIds.userNo.get();
        return orderRejectionService.addOrderRejection(orderRejectionAddDTO, userNo);
    }
}

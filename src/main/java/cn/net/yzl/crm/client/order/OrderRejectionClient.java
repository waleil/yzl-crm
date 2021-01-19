package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 拒收单客户端
 *
 * @author zhouchangsong
 */
@FeignClient(name = "orderRejection", url = "${api.gateway.url}/orderService/orderRejection")
public interface OrderRejectionClient {

    /**
     * 查询拒收单分页
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("v1/getOrderRejectionList")
    ComResponse<Page<OrderRejectionPageDTO>> getOrderRejectionList(@RequestParam(name = "orderNo") String orderNo, @RequestParam(name = "pageNum") Integer pageNum, @RequestParam(name = "pageSize") Integer pageSize);

    /**
     * 新增拒收单
     *
     * @param dto
     * @return
     */
    @PutMapping("v1/addOrderRejection")
    ComResponse<?> addOrderRejection(@Valid @RequestBody OrderRejectionAddDTO dto);

    /**
     * 根据订单号查询拒收订单优惠
     *
     * @param orderNo
     * @return
     */
    @GetMapping("v1/getCouponListByOrderNo")
    ComResponse<List<OrderCouponDTO>> getListByOrderNo(@RequestParam(name = "orderNo") String orderNo);


    @GetMapping("v1/getOrderList")
    @ApiOperation(value = "查询已发货、待发货订单")
    ComResponse<Page<OderListResDTO>> selectOrderList(@ApiParam(name = "pageNum", value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                      @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                      @ApiParam(name = "orderNo", value = "订单号") @RequestParam("orderNo") String orderNo);

    /**
     * 查询拒收单详情
     * @param orderNo
     * @return
     */
    @GetMapping("v1/getOrderRejectionDetail")
    @ApiOperation(value = "查询拒收单详情")
    ComResponse<OrderRejectionDetailDTO> getOrderRejectionDetail(@ApiParam(name = "orderNo", value = "订单号") @RequestParam("orderNo") String orderNo);
}

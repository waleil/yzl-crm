package cn.net.yzl.crm.client.order;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import cn.net.yzl.order.model.vo.order.OrderCouponDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionAddDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderRejectionPageDTO;

/**
 * 拒收单客户端
 *
 * @author zhouchangsong
 */
@FeignClient(name = "orderRejection", url = "localhost:4455/orderRejection")
public interface OrderRejectionClient {

    /**
     * 查询拒收单分页
     *
     * @param orderNo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("v1/getOrderRejectionList")
    ComResponse<Page<OrderRejectionPageDTO>> getOrderRejectionList(@RequestParam String orderNo, @RequestParam Integer pageNo, @RequestParam Integer pageSize);

    /**
     * 新增拒收单
     *
     * @param dto
     * @return
     */
    @PostMapping("v1/addOrderRejection")
    ComResponse<?> addOrderRejection(@Valid @RequestBody OrderRejectionAddDTO dto);

    /**
     * 根据订单号查询拒收订单优惠
     *
     * @param orderNo
     * @return
     */
    @GetMapping("v1/getCouponListByOrderNo")
    ComResponse<List<OrderCouponDTO>> getListByOrderNo(@RequestParam String orderNo);


    /**
     * @param pageNo
     * @param pageSize
     * @param orderNo
     * @return 查询已发货、待发货订单
     * @date 2021年1月25日,下午1:23:48
     */
    @GetMapping("v1/getOrderList")
    ComResponse<Page<OderListResDTO>> selectOrderList(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String orderNo);

    /**
     * 查询拒收单详情
     *
     * @param orderNo
     * @return
     */
    @GetMapping("v1/getOrderRejectionDetail")
    ComResponse<OrderRejectionDetailDTO> getOrderRejectionDetail(@RequestParam String orderNo);
}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.order.IOrderOprService;
import cn.net.yzl.order.model.vo.order.OrderOprDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("orderOpr")
@Api(tags = "订单管理")
public class OrderOprController {



    @Autowired
    private IOrderOprService orderOprService;
    /**
     * 取消订单
     *
     * @param orderNo
     * @return
     */
    @ApiOperation(value = "取消订单")
    @PostMapping("v1/cancleOrderM")
    public ComResponse cancleOrderM(@RequestBody @Valid OrderOprDTO dto) {

        return this.orderOprService.cancleOrder(dto.getOrderNo());
    }
}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.client.order.OrderRejectionClient;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.order.OrderRejectionService;
import cn.net.yzl.order.model.vo.order.OrderRejectionPageDTO;
import cn.net.yzl.order.service.OrderSaleService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ComResponse<Page<OrderRejectionPageDTO>> getOrderRejectionList(@ApiParam(name = "orderNo", value = "订单号") @RequestParam("orderNo") String orderNo,
                                                                          @ApiParam(name = "pageNum", value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                          @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return orderRejectionClient.getOrderRejectionList(orderNo, pageNum, pageSize);
    }

    @PutMapping("v1/addOrderRejection")
    @ApiOperation(value = "新增拒收订单")
    public ComResponse addOrderRejection(HttpServletRequest request, @Valid @RequestBody OrderRejectionAddDTO orderRejectionAddDTO) {
        String userNo = request.getHeader("userNo");


        return orderRejectionService.addOrderRejection(orderRejectionAddDTO,userNo);
    }
}

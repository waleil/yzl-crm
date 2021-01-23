package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.service.order.INewOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.InputStream;

//import cn.net.yzl.order.model.vo.order.NewOrderExcelInDTO;

@RestController
@Slf4j
@RequestMapping("newOrdder")
@Api(tags = "订单管理")
public class NewOrderController {

    @Autowired
    private MemberGroupFeign memberFien;

    @Autowired
    private INewOrderService newOrderService;

    @ApiOperation(value = "新建订单（会刊）")
    @PostMapping("v1/newOrder")
    public ComResponse<Boolean> newOrder(@RequestBody NewOrderDTO dto, HttpServletRequest request) {
        ComResponse<Boolean> response = null;
        try {
            String userNo = request.getHeader("userNo");
            dto.setUserNo(userNo);
            response = newOrderService.newOrder(dto);
        } catch (BizException e) {
            throw new BizException(e.getCode(), e.getMessage());
        }
        return response;
    }



}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.service.order.INewOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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

    @ApiOperation(value = "查询群组列表",httpMethod = "POST")
    @PostMapping(value = "v1/getCrowdGroupByPaget")
    public ComResponse<Page<member_crowd_group>> getCrowdGroupByPaget(@RequestBody CrowdGroupDTO crowdGroupDTO) {

        return memberFien.getCrowdGroupByPage(crowdGroupDTO);
    }

}

package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderAccountConfirmClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.order.OrderAccountConfirmService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderAccountConfirmVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 收款确认单表(OrderAccountConfirm)表控制层
 *
 * @author chengyu
 * @since 2021-02-02 13:52:37
 */

@Api(tags = "结算中心")
@RestController
@RequestMapping("orderAccountConfirm")
public class OrderAccountConfirmController {

    @Resource
    private OrderAccountConfirmClient client;

    @Resource
    private OrderAccountConfirmService service;

    @Resource
    private FastDFSConfig fastDFSConfig;


    @ApiOperation(value = "根据订单号查询收款确认单")
    @GetMapping("v1/selectByOrderNo")
    public ComResponse<OrderAccountConfirmVO> selectByOrderNo(@RequestParam("orderNo")
                                                                @NotNull(message = "订单编号不能为空")
                                                                @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo) {

        ComResponse<OrderAccountConfirmVO> result = this.client.selectByOrderNo(orderNo);
        if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(result.getCode())) {
            throw new BizException(result.getCode(),result.getMessage());
        }
        result.getData().setDsfPath(fastDFSConfig.getUrl());
        return result;
    }


    /**
     * 保存确认收款记录
     *
     * @param
     * @return 单条数据
     */
    @ApiOperation(value = "保存收款确认单")
    @PostMapping("v1/saveOrderAccountConfirm")
    public ComResponse<Boolean> saveOrderAccountConfirm(@RequestBody @Valid OrderAccountConfirmVO vo) {
        return this.service.saveOrderAccountConfirm(vo);
    }

}
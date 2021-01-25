package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.OrderDistributeExpressDTO;
import cn.net.yzl.model.dto.OrderDistributeExpressRuleDetailDTO;
import cn.net.yzl.model.dto.OrderDistributeExpressRuleListDTO;
import cn.net.yzl.model.dto.OrderExpressDTO;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wujianing
 * @version 1.0
 * @date: 2021/1/23 14:00
 * @title: OrderDistributeExpressFeignService
 * @description:
 */
@FeignClient(name = "orderDistributeExpressClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface OrderDistributeExpressFeignService {

    @PostMapping(value = "orderDistributeExpress/v1/selectOrderDistributeExpressRuleList")
    @ApiOperation("智能分配快递规则列表查询")
    public ComResponse<List<OrderDistributeExpressRuleListDTO>> selectOrderDistributeExpressRuleList();

    @PostMapping(value = "orderDistributeExpress/v1/selectOrderDistributeExpressRuleDetail")
    @ApiOperation("智能分配快递规则详情查询")
    public ComResponse<OrderDistributeExpressRuleDetailDTO> selectOrderDistributeExpressRuleDetail(@RequestParam("id") Integer id);

    @PostMapping(value = "orderDistributeExpress/v1/insertOrderDistributeExpressRule")
    @ApiOperation("智能分配快递规则新增")
    public ComResponse insertOrderDistributeExpressRule(@RequestBody OrderDistributeExpressRuleEditVO vo);

    @PostMapping(value = "orderDistributeExpress/v1/updateOrderDistributeExpressRule")
    @ApiOperation("智能分配快递规则修改")
    public ComResponse updateOrderDistributeExpressRule(@RequestBody OrderDistributeExpressRuleEditVO vo);

    @PostMapping(value = "orderDistributeExpress/v1/updateOrderDistributeExpressRuleStatus")
    @ApiOperation("智能分配快递规则状态修改")
    public ComResponse updateOrderDistributeExpressRuleStatus(@RequestBody OrderDistributeExpressRuleStatusVO vo);

    @PostMapping(value = "orderDistributeExpress/v1/updateOrderDistributeExpressRuleResult")
    @ApiOperation("智能分配")
    public ComResponse updateOrderDistributeExpressRuleResult(@RequestBody List<OrderExpressDTO> orderExpressList);

    @PostMapping(value = "orderDistributeExpress/v1/selectOrderDistributeExpressList")
    @ApiOperation("订单分配快递列表分页查询")
    public ComResponse<Page<OrderDistributeExpressDTO>> selectOrderDistributeExpressList(@RequestBody OrderDistributeExpressVO orderDistributeExpressVO);

    @PostMapping(value = "orderDistributeExpress/v1/updateOrderDistributeExpressStatus")
    @ApiOperation("取消订单")
    public ComResponse updateOrderDistributeExpressStatus(@RequestBody OrderDistributeExpressStatusVO vo);

    @PostMapping(value = "orderDistributeExpress/v1/updateOrderDistributeExpressByMan")
    @ApiOperation("人工分配")
    public ComResponse updateOrderDistributeExpressByMan(@RequestBody OrderDistributeExpressByManVO vo);

    @PostMapping(value = "orderDistributeExpress/v1/insertOrderDataToDistributeExpress")
    @ApiOperation("获取订单服务数据并保存")
    public ComResponse insertOrderDataToDistributeExpress();

}

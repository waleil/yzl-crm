package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.OrderDistributeExpressFeignService;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.OrderDistributeExpressService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.dto.OrderDistributeExpressDTO;
import cn.net.yzl.model.dto.OrderDistributeExpressRuleDetailDTO;
import cn.net.yzl.model.dto.OrderDistributeExpressRuleListDTO;
import cn.net.yzl.model.dto.express.ExpressImportModel;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author wujianing
 * @version 1.0
 * @date: 2021/1/23 14:17
 * @title: OrderDistributeExpressController
 * @description:
 */
@RestController
@Api(value = "仓储中心心心心心-订单分配快递", tags = {"仓储中心心心心心-订单分配快递"})
@RequestMapping("orderDistributeExpress")
public class OrderDistributeExpressController {

    @Autowired
    private OrderDistributeExpressFeignService orderDistributeExpressFeignService;
    @Autowired
    private OrderDistributeExpressService orderDistributeExpressService;

    @PostMapping(value = "v1/selectOrderDistributeExpressRuleList")
    @ApiOperation("智能分配快递规则列表查询")
    public ComResponse<List<OrderDistributeExpressRuleListDTO>> selectOrderDistributeExpressRuleList() {
        return orderDistributeExpressFeignService.selectOrderDistributeExpressRuleList();
    }

    @GetMapping(value = "v1/selectOrderDistributeExpressRuleDetail")
    @ApiOperation("智能分配快递规则详情查询")
    public ComResponse<OrderDistributeExpressRuleDetailDTO> selectOrderDistributeExpressRuleDetail(@RequestParam("id") Integer id) {
        return orderDistributeExpressFeignService.selectOrderDistributeExpressRuleDetail(id);
    }

    @PostMapping(value = "v1/insertOrderDistributeExpressRule")
    @ApiOperation("智能分配快递规则新增")
    public ComResponse insertOrderDistributeExpressRule(@RequestBody OrderDistributeExpressRuleEditVO vo) {
        return orderDistributeExpressFeignService.insertOrderDistributeExpressRule(vo);
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressRule")
    @ApiOperation("智能分配快递规则修改")
    public ComResponse updateOrderDistributeExpressRule(@RequestBody OrderDistributeExpressRuleEditVO vo) {
        return orderDistributeExpressFeignService.updateOrderDistributeExpressRule(vo);
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressRuleStatus")
    @ApiOperation("智能分配快递规则状态修改")
    public ComResponse updateOrderDistributeExpressRuleStatus(@RequestBody OrderDistributeExpressRuleStatusVO vo) {
        return orderDistributeExpressFeignService.updateOrderDistributeExpressRuleStatus(vo);
    }

    @PostMapping(value = "v1/batchUpdateOrderDistributeExpressRuleStatus")
    @ApiOperation("批量检测并修改规则状态")
    public ComResponse batchUpdateOrderDistributeExpressRuleStatus() {
        return orderDistributeExpressFeignService.batchUpdateOrderDistributeExpressRuleStatus();
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressRuleResult")
    @ApiOperation("智能分配")
    public ComResponse updateOrderDistributeExpressRuleResult(@RequestBody DistributeExpressVO vo) {
        vo.setUpdateUser(QueryIds.userNo.get());
        return orderDistributeExpressFeignService.updateOrderDistributeExpressRuleResult(vo);
    }

    @PostMapping(value = "v1/selectOrderDistributeExpressList")
    @ApiOperation("订单分配快递列表分页查询")
    public ComResponse<Page<OrderDistributeExpressDTO>> selectOrderDistributeExpressList(@RequestBody OrderDistributeExpressListVO vo) {
        return orderDistributeExpressFeignService.selectOrderDistributeExpressList(vo);
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressStatus")
    @ApiOperation("修改订单状态")
    public ComResponse updateOrderDistributeExpressStatus(@RequestBody OrderDistributeExpressStatusVO vo) {
        return orderDistributeExpressFeignService.updateOrderDistributeExpressStatus(vo);
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressRegisterInfo")
    @ApiOperation("异常登记")
    public ComResponse updateOrderDistributeExpressRegisterInfo(@RequestBody OrderDistributeExpressRegisterVO vo){
        return orderDistributeExpressFeignService.updateOrderDistributeExpressRegisterInfo(vo);
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressByMan")
    @ApiOperation("人工分配")
    public ComResponse updateOrderDistributeExpressByMan(@RequestBody OrderDistributeExpressByManVO vo) {
        vo.setUpdateUser(QueryIds.userNo.get());
        return orderDistributeExpressFeignService.updateOrderDistributeExpressByMan(vo);
    }

    @PostMapping(value = "v1/insertOrderDistributeExpress")
    @ApiOperation("订单分配快递数据插入")
    public ComResponse insertOrderDistributeExpress(@RequestBody List<OrderDistributeExpressVO> voList){
        return orderDistributeExpressFeignService.insertOrderDistributeExpress(voList);
    }

    @GetMapping(value = "v1/selectExpressCompanyList")
    @ApiOperation("获取物流服务物流公司列表")
    public ComResponse selectExpressCompanyList() {
        return orderDistributeExpressFeignService.selectExpressCompanyList();
    }

    @PostMapping(value = "v1/updateOrderDistributeExpressStatusToCancel")
    @ApiOperation("取消订单")
    public ComResponse updateOrderDistributeExpressStatusToCancel(@RequestBody OrderCancelStatusVO vo) {
        return orderDistributeExpressFeignService.updateOrderDistributeExpressStatusToCancel(vo);
    }

    @ApiOperation(value = "导入快递信息",notes = "导入快递信息")
    @PostMapping("v1/readExpressExcelInfo")
    public ComResponse readExpressExcelInfo(@RequestParam("file") MultipartFile file) {
        return orderDistributeExpressService.readExpressExcelInfo(file);
    }
}

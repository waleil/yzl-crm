package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "settlementFein", url = "${api.gateway.url}/orderService/SettlementFein")
public interface SettlementFein {

    @PostMapping("v1/createSettlement")
    @ApiOperation("生成结算单")
    public ComResponse<Boolean> createSettlement(@RequestBody @Valid SettlementReqDTO dto);

    @PostMapping("v1/settlement")
    @ApiOperation("结算")
    public ComResponse<Boolean> settlement(@RequestBody @Valid SettlementFlowDTO dto);
    @PostMapping("v1/settlementAmtLeft")
    @ApiOperation("查询剩余可结算金额 元")
    public ComResponse<String> settlementAmtLeft(@RequestParam("settlementCode")
                                                 @NotNull(message = "结算单编号")
                                                 @ApiParam(name = "settlementCode", value = "结算单编号", required = true)String settlementCode);

    @PostMapping("v1/settlementFlow")
    @ApiOperation("查询结算流水")
    public ComResponse<List<SettlementFlowDTO>> settlementFlow(@RequestParam("settlementCode")
                                                               @NotNull(message = "结算单编号")
                                                               @ApiParam(name = "settlementCode", value = "结算单编号", required = true)String settlementCode);
    @PostMapping("v1/settlementList")
    @ApiOperation("查询结算列表")
    public ComResponse<Page<SettlementDTO>> settlementList(@RequestBody SettlementListReqDTO dto);

    @PostMapping("v1/settlementTotal")
    @ApiOperation("查询结算列表统计信息")
    public ComResponse<SettlementDTO> settlementTotal(@RequestBody SettlementListReqDTO dto);

    @PostMapping("v1/exportSettlementList")
    @ApiOperation("导出结算列表")
    public ComResponse<List<SettlementDTO>> exportSettlementList(@RequestBody SettlementListReqDTO dto);

    @PostMapping("v1/getSettlementDetailGroupByOrderNo")
    @ApiOperation("根据订单编号查询订单明细信息，并去重")
    ComResponse<List<SettlementDetailDistinctListDTO>> getSettlementDetailGroupByOrderNo(@RequestBody List<String> orderNoList);
}

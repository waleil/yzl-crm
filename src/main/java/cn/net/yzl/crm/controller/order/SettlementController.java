package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单结算单
 *
 * @author chengyu
 * @since 2021-02-05 20:40:53
 */
@RestController
@RequestMapping("settlement")
@Api(tags = "结算中心")
public class SettlementController {

	@Resource
	private SettlementFein settlementFein;
	@Resource
	private EhrStaffClient ehrStaffClient;

	@PostMapping("v1/createSettlement")
	@ApiOperation("生成结算单")
	public ComResponse<Boolean> createSettlement(@RequestBody @Valid SettlementReqDTO dto) {
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			throw new BizException(sresponse.getCode(), sresponse.getMessage());
		}
		dto.setOprCode(sresponse.getData().getStaffNo());
		dto.setOprName(sresponse.getData().getName());
		return settlementFein.createSettlement(dto);

	}

	@PostMapping("v1/validcreatesettlement")
	@ApiOperation("生成结算单校验")
	public ComResponse<Boolean> validCreateSettlement(@RequestBody SettlementReqDTO dto) {
		return settlementFein.validCreateSettlement(dto);

	}

	@PostMapping("v1/settlement")
	@ApiOperation("结算")
	public ComResponse<Boolean> settlement(@RequestBody @Valid SettlementFlowDTO dto) {
		ComResponse<StaffImageBaseInfoDto> sresponse = this.ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		// 如果服务调用异常
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(sresponse.getCode())) {
			throw new BizException(sresponse.getCode(), sresponse.getMessage());
		}
		dto.setSettlementStaffCode(sresponse.getData().getStaffNo());
		dto.setSettlementStaffName(sresponse.getData().getName());
		return settlementFein.settlement(dto);

	}

	@PostMapping("v1/settlementAmtLeft")
	@ApiOperation("查询剩余可结算金额 元")
	public ComResponse<String> settlementAmtLeft(
			@RequestParam("settlementCode") @NotNull(message = "结算单编号") @ApiParam(name = "settlementCode", value = "结算单编号", required = true) String settlementCode) {

		return settlementFein.settlementAmtLeft(settlementCode);

	}

	@PostMapping("v1/settlementFlow")
	@ApiOperation("查询结算流水")
	public ComResponse<List<SettlementFlowDTO>> settlementFlow(
			@RequestParam("settlementCode") @NotNull(message = "结算单编号") @ApiParam(name = "settlementCode", value = "结算单编号", required = true) String settlementCode) {

		return settlementFein.settlementFlow(settlementCode);

	}

	@PostMapping("v1/settlementList")
	@ApiOperation("查询结算列表")
	public ComResponse<Page<SettlementDTO>> settlementList(@RequestBody SettlementListReqDTO dto) {

		return settlementFein.settlementList(dto);

	}

	@PostMapping("v1/settlementTotal")
	@ApiOperation("查询结算列表统计信息")
	public ComResponse<SettlementDTO> settlementTotal(@RequestBody SettlementListReqDTO dto) {

		return settlementFein.settlementTotal(dto);

	}

	@GetMapping("v1/selectSettleProductList")
	@ApiOperation("查询结算商品明细")
	public ComResponse<Page<SettlementProductDetailDTO>> selectSettleProductList(
			@RequestParam(value = "settlementCode") String settlementCode,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		return settlementFein.selectSettleProductList(settlementCode, pageNo, pageSize);
	}

}
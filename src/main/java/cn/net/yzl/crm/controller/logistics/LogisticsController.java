package cn.net.yzl.crm.controller.logistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.logistics.model.ExpressCompany;
import cn.net.yzl.logistics.model.ExpressFindTraceDTO;
import cn.net.yzl.logistics.model.ExpressTraceResDTO;
import cn.net.yzl.logistics.model.TransPortExceptionRegistry;
import cn.net.yzl.logistics.model.pojo.ExpressCompanyBankInfoDTO;
import cn.net.yzl.logistics.model.pojo.ExpressCompanySaveDTO;
import cn.net.yzl.logistics.model.pojo.ExpressCompanyStateInfo;
import cn.net.yzl.logistics.model.pojo.ExpressForInterfaceSaveDTO;
import cn.net.yzl.logistics.model.pojo.ExpressSearchDTO;
import cn.net.yzl.logistics.model.pojo.ObjectCommon;
import cn.net.yzl.logistics.model.vo.ExpressCode;
import cn.net.yzl.logistics.model.vo.ExpressCodeVo;
import cn.net.yzl.logistics.model.vo.ExpressTraceNumSearchVo;
import cn.net.yzl.logistics.model.vo.InterFaceInfo;
import cn.net.yzl.logistics.model.vo.LogisticsOrderInofoReturn;
import cn.net.yzl.logistics.model.vo.RegistryOrderinfo;
import cn.net.yzl.logistics.model.vo.SExceptionCondition;
import cn.net.yzl.logistics.model.vo.StoreToLogisticsDtoTrace;
import cn.net.yzl.model.dto.StoreToLogisticsDto;
import cn.net.yzl.model.pojo.StorePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("logistics")
@Api(tags = "????????????")
public class LogisticsController {

	@Autowired
	private LogisticsFien logisticsFien;

	@Autowired
	private EhrStaffClient ehrStaffClient;

	@Autowired
	private FastdfsUtils fastdfsUtils;
	@Autowired
	private FastDFSConfig fastDFSConfig;

	@Autowired
	private StoreFeginService storeFeginService;

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/search/orderexpress")
	public ComResponse<Page<LogisticsOrderInofoReturn>> searchOrderExpress(
			@RequestBody @Valid ExpressTraceNumSearchVo searchVo) {

		return logisticsFien.searchOrderExpress(searchVo);
	}

	@ApiOperation(value = "????????????")
	@PostMapping("v1/signed/order")
	public ComResponse<Boolean> signedOrder(
			@RequestBody @Valid List<StoreToLogisticsDtoTrace> storeToLogisticsDtoTrace) {
		ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());

		if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
		}
		if (null == userNo.getData()) {
			return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????" + userNo.getCode());
		}

		StaffImageBaseInfoDto data = userNo.getData();

		if (StringUtils.isEmpty(data.getName())) {
			return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
		}
		Date now = new Date();
		String userName = data.getName();
		String template = userName + "?????????????????????";
		for (StoreToLogisticsDtoTrace trace : storeToLogisticsDtoTrace) {
			// ?????????????????????????????????
			if (null == (trace.getSupplementRegistry())
					|| StringUtils.isEmpty(trace.getSupplementRegistry().getExpressNum()))
				return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE); // ???????????????????????????
			// ?????????????????????????????????
			if (null == (trace.getTraceInfo()))
				return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE); // ???????????????????????????

			trace.getTraceInfo().setDescription(template);
			trace.getSupplementRegistry().setSupplementorName(userName);
			trace.getSupplementRegistry().setSupplementor(data.getStaffNo()); // ????????? user code
			trace.getSupplementRegistry().setSignTime(now); // signTime
			trace.getSupplementRegistry().setDepartId(data.getDepartId());
			trace.getSupplementRegistry().setOrderStatus(5);
		}

		return logisticsFien.signedOrder(storeToLogisticsDtoTrace);
	}

	@ApiOperation(value = "????????????")
	@PostMapping("v1/cancel/signed/order")
	public ComResponse<Boolean> cancelSignOrder(@RequestBody @Valid StoreToLogisticsDtoTrace storeToLogisticsDtoTrace) {

		ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
		if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
		}
		if (null == userNo.getData()) {
			return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????" + userNo.getCode());
		}

		StaffImageBaseInfoDto data = userNo.getData();

		if (StringUtils.isEmpty(data.getName())) {
			return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
		}

		// ????????????
		if (null == storeToLogisticsDtoTrace)
			return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE);

		storeToLogisticsDtoTrace.getSupplementRegistry().setSupplementor(data.getName());
		// ?????????????????????????????????
		if (null == storeToLogisticsDtoTrace.getSupplementRegistry()
				|| StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getExpressNum())
				|| StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getSupplementor()))
			return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE); // ???????????????????????????

		// ????????????
		if (StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getSupplementor()))
			return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);

		// ?????????????????????????????????
		if (null == storeToLogisticsDtoTrace.getTraceInfo())
			return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE); // ???????????????????????????

		String userName = data.getName();
		String template = userName + "???????????????????????????";
		storeToLogisticsDtoTrace.getTraceInfo().setDescription(template);

		storeToLogisticsDtoTrace.getSupplementRegistry().setSupplementor(data.getStaffNo()); // user code
		storeToLogisticsDtoTrace.getSupplementRegistry().setSignTime(new Date()); // signTime
		storeToLogisticsDtoTrace.getSupplementRegistry().setDepartId(data.getDepartId());
		storeToLogisticsDtoTrace.getSupplementRegistry().setOrderStatus(1);

		return logisticsFien.cancelSignOrder(storeToLogisticsDtoTrace);

	}

	@ApiOperation(value = "????????????????????????")
	@GetMapping("v1/like/search/expresscompany")
	public ComResponse<List<ObjectCommon>> getCompanyByName(@RequestParam("companyName") String companyName) {
		return logisticsFien.getCompanyByName(companyName);
	}

	@ApiOperation(value = "??????????????????")
	@GetMapping("/v1/express/company/interfaceinfo/list")
	public ComResponse<List<InterFaceInfo>> listInterfaceInfo(@RequestParam("id") String id) {
		return logisticsFien.listInterfaceInfo(id);
	}

	@ApiOperation(value = "??????????????????", notes = "")
	@PostMapping("findLogisticsTraces")
	public GeneralResult<List<ExpressTraceResDTO>> findLogisticsTraces(@RequestBody @Valid ExpressFindTraceDTO dto) {
		return logisticsFien.findLogisticsTraces(dto);
	}

	@ApiOperation(value = "??????-????????????")
	@GetMapping("v1/generateBillOrderNo")
	public ComResponse<StoreToLogisticsDto> generateBillOrderNo(@RequestParam("orderNo") String orderNo) {

		RegistryOrderinfo registryOrderinfo = new RegistryOrderinfo();

		try {
			ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
			if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
			}
			if (null == (userNo.getData())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????" + userNo.getCode());
			}

			StaffImageBaseInfoDto data = userNo.getData();

			if (StringUtils.isEmpty(data.getName())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
			}

			registryOrderinfo.setOrderNO(orderNo);
			registryOrderinfo.setRegisterName(data.getName());

		} catch (BizException e) {
			ComResponse.fail(ComResponse.ERROR_STATUS, "?????????????????????");
		}

		return logisticsFien.generateBillOrderNo(registryOrderinfo);
	}

	@ApiOperation(value = "??????-????????????")
	@PostMapping("v1/searcha/exception")
	public ComResponse<Page<TransPortExceptionRegistry>> selectExceptionByCondition(
			@RequestBody SExceptionCondition sExceptionCondition) {

		return logisticsFien.selectExceptionByCondition(sExceptionCondition);
	}

	@ApiOperation(value = "??????-??????????????????")
	@PostMapping("v1/cancel/registry/exceptioninfo")
	public ComResponse<Boolean> cancelRegistryException(@RequestParam("id") String id) {
		return logisticsFien.cancelRegistryException(id);

	}

	@ApiOperation(value = "??????-??????????????????")
	@PostMapping("v1/cancelbatch/registry/exceptioninfo")
	public ComResponse<Boolean> cancelBatchRegistryException(@RequestBody List<String> ids) {
		return logisticsFien.cancelBatchRegistryException(ids);
	}

	@ApiOperation(value = "????????????")
	@GetMapping("/fastDfs/download")
	public String downloadFile(@RequestParam(value = "id") String id) throws IOException {

		return logisticsFien.downLoadContract(id);

	}

	@ApiOperation(value = "????????????")
	@PostMapping("/fastDfs/upload")
	public ComResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

		StorePath upload = fastdfsUtils.upload(file);

		String path = fastDFSConfig.getUrl() + "/" + upload.getFullPath();

		return ComResponse.success(path);
	}

	@ApiOperation(value = "??????????????????????????????")
	@PostMapping("v1/expresscompany/listPage")
	public ComResponse<Page<ExpressCompany>> listPage(@RequestBody @Valid ExpressSearchDTO expressSearchDTO) {
		ComResponse<List<StorePo>> list=null;
		if (!StringUtils.isEmpty(expressSearchDTO.getWarehouseId())) {
			list = storeFeginService.selectStoreAny();
		}
//		ComResponse<List<StorePo>> list = storeFeginService.selectStoreAny();

		if (list!=null && list.getCode() == 200) {

			List<String> storePoList = new ArrayList<>();
			if (expressSearchDTO.getWarehouseId() != null && expressSearchDTO.getWarehouseId()!=""
			&& !StringUtils.isEmpty(expressSearchDTO.getWarehouseId())
			) {
				storePoList = Optional.ofNullable(list.getData())
						.orElseGet(ArrayList::new).stream()
						.filter(p -> p.getName().indexOf(expressSearchDTO.getWarehouseId()) > -1)
						.map(StorePo::getNo)
						.collect(Collectors.toList());
			}

			expressSearchDTO.setWarehouseId(StringUtils.join(storePoList.toArray(), ","));
		} else {
			expressSearchDTO.setWarehouseId(StringUtils.join(Collections.EMPTY_LIST.toArray(), ","));
		}

		return logisticsFien.listPage(expressSearchDTO);
	}

	@ApiOperation(value = "???????????????????????????")
	@PostMapping("v1/express/company/list")
	public ComResponse viewExpressCompany() {
		return logisticsFien.viewExpressCompany();

	}

	@ApiOperation(value = "??????????????????")
	@GetMapping("v1/view")
	public ComResponse view(@RequestParam("id") @Valid String id) {
		return logisticsFien.view(id);
	}

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/update")
	public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO) {

		String userName = "";
		try {
			ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
			if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
			}
			if (null == (userNo.getData())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????" + userNo.getCode());
			}

			StaffImageBaseInfoDto data = userNo.getData();

			if (StringUtils.isEmpty(data.getName())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
			}

			userName = data.getName();
		} catch (BizException e) {
			ComResponse.fail(ComResponse.ERROR_STATUS, "?????????????????????");
		}
		saveDTO.setOperator(userName);
		saveDTO.setUpdateCode(userName);

		return logisticsFien.update(saveDTO);

	}

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/save")
	public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO) {

		String userName = "";
		try {
			ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
			if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
			}
			if (null == (userNo.getData())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????" + userNo.getCode());
			}

			StaffImageBaseInfoDto data = userNo.getData();

			if (StringUtils.isEmpty(data.getName())) {
				return ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
			}

			userName = data.getName();
		} catch (BizException e) {
			ComResponse.fail(ComResponse.ERROR_STATUS, "?????????????????????");
		}
		saveDTO.setOperator(userName);
		saveDTO.setUpdateCode(userName);
		saveDTO.setCreateCode(userName);

		return logisticsFien.save(saveDTO);
	}

	@ApiOperation(value = "???????????????????????????")
	@PostMapping("v1/save/account")
	public ComResponse<Boolean> saveAccountInfo(@RequestBody @Valid ExpressCompanyBankInfoDTO saveDTO) {

		return logisticsFien.saveAccountInfo(saveDTO);

	}

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/update/state")
	public ComResponse<Boolean> updateState(@RequestBody @Valid ExpressCompanyStateInfo saveDTO) {
		return logisticsFien.updateState(saveDTO);
	}

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/manage/interface")
	public ComResponse<Boolean> manageInterface(@RequestBody @Valid ExpressForInterfaceSaveDTO saveDTO) {

		return logisticsFien.manageInterface(saveDTO);

	}

	@ApiOperation(value = "??????????????????")
	@PostMapping("v1/deleteById")
	public GeneralResult<Integer> deleteById(
			@RequestParam("id") @NotBlank(message = "????????????id????????????") @ApiParam(name = "id", value = "????????????id", required = true) Integer id) {
		return logisticsFien.deleteById(id);
	}

	@ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????")
	@GetMapping("v1/selectExpressComponyCode")
	public ComResponse<List<ExpressCodeVo>> selectExpressComponyCode() {
		return logisticsFien.selectExpressComponyCode();
	}

	@ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????")
	@GetMapping("v1/selectExpressComponyCodeForObject")
	public ComResponse<ExpressCode> selectExpressComponyCodeForObject() {
		return logisticsFien.selectExpressComponyCodeForObject();
	}

	@ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????")
	@GetMapping("v1/selectExpressComponyDetail")
	public ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail() {

		return logisticsFien.selectExpressComponyDetail();
	}

}

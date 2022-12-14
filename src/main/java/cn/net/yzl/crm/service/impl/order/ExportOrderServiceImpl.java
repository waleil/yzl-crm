package cn.net.yzl.crm.service.impl.order;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.order.GoodsInTransitFeign;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.order.SettlementFein;
import cn.net.yzl.crm.service.order.ExportOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.model.mongo.order.GoodsInTransit;
import cn.net.yzl.order.model.mongo.order.GoodsInTransit4Excel;
import cn.net.yzl.order.model.vo.order.GoodsInTransitReqDTO;
import cn.net.yzl.order.model.vo.order.OrderSellDetailReqDTO;
import cn.net.yzl.order.model.vo.order.OrderSellDetailResDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedReqDTO;
import cn.net.yzl.order.model.vo.order.ProductDetailSettlementedResDTO;
import cn.net.yzl.order.model.vo.order.Settlement4ExportDTO;
import cn.net.yzl.order.model.vo.order.SettlementDTO;
import cn.net.yzl.order.model.vo.order.SettlementListReqDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExportOrderServiceImpl implements ExportOrderService {

	@Resource
	private SettlementFein settlementFein;
	@Resource
	private GoodsInTransitFeign goodsInTransitFeign;

	@Autowired
	private OrderSearchClient orderSearchClient;

	@Override
	public void exportSettlementList(SettlementListReqDTO dto, HttpServletResponse response) {
		ExcelWriter excelWriter = null;
		try {
			dto.setPageNo(1);// ?????????1???
			dto.setPageSize(1000);// ????????????1000?????????
			ComResponse<Page<SettlementDTO>> data = this.settlementFein.settlementList(dto);
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
				log.error("????????????????????????>>>{}", data);
				throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(), "??????????????????????????????" + data.getMessage());
			}
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			String title = new String("????????????".getBytes(), StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
					URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));

			Page<SettlementDTO> page = data.getData();
			PageParam param = page.getPageParam();
			if (param.getPageTotal() == 0) {
				excelWriter = EasyExcel.write(response.getOutputStream(), Settlement4ExportDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????

				excelWriter.write(Arrays.asList(new Settlement4ExportDTO()), writeSheet);

			} else {

				excelWriter = EasyExcel.write(response.getOutputStream(), Settlement4ExportDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????
				List<Settlement4ExportDTO> settlement4ExportDTOS = this.formatesettleData(page.getItems());
				excelWriter.write(settlement4ExportDTOS, writeSheet);
				settlement4ExportDTOS.clear();
				page.getItems().clear();// ???????????????????????????
				// ?????????????????????1
				if (param.getPageTotal() > 1) {
					// ??????????????????????????????
					for (int i = 2; i <= param.getPageTotal(); i++) {
						dto.setPageNo(i);
						data = this.settlementFein.settlementList(dto);
						if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
							log.error("????????????????????????>>>{}", data);
							return;
						}
						page = data.getData();
						settlement4ExportDTOS = this.formatesettleData(page.getItems());
						excelWriter.write(settlement4ExportDTOS, writeSheet);
						settlement4ExportDTOS.clear();
						page.getItems().clear();// ???????????????????????????
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Override
	public void exportSelectProductDetailBySettledOrder(ProductDetailSettlementedReqDTO dto,
			HttpServletResponse response) {
		if (dto.getStartCreateTime() == null && dto.getEndCreateTime() == null) {
			dto.setEndCreateTime(new Date());
			dto.setStartCreateTime(DateFormatUtil.addMonth(new Date(), -12));
		}
		ExcelWriter excelWriter = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			String title = new String("?????????????????????".getBytes(), StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
					URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));

			dto.setPageNo(1);// ?????????1???
			dto.setPageSize(1000);// ????????????1000?????????
			ComResponse<Page<ProductDetailSettlementedResDTO>> data = this.settlementFein
					.selectProductDetailBySettledOrder(dto);
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
				log.error("?????????????????????????????????>>>{}", data);
				throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(), "?????????????????????????????????" + data.getMessage());
			}
			Page<ProductDetailSettlementedResDTO> page = data.getData();
			PageParam param = page.getPageParam();
			if (param.getPageTotal() == 0) {
				excelWriter = EasyExcel.write(response.getOutputStream(), ProductDetailSettlementedResDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????

				excelWriter.write(page.getItems(), writeSheet);

			} else {

				excelWriter = EasyExcel.write(response.getOutputStream(), ProductDetailSettlementedResDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????
				excelWriter.write(data.getData().getItems(), writeSheet);
				page.getItems().clear();// ???????????????????????????
				// ?????????????????????1
				if (param.getPageTotal() > 1) {
					// ??????????????????????????????
					for (int i = 2; i <= param.getPageTotal(); i++) {
						dto.setPageNo(i);
						data = this.settlementFein.selectProductDetailBySettledOrder(dto);
						if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
							log.error("?????????????????????????????????>>>{}", data);
							return;
						}
						page = data.getData();
						excelWriter.write(page.getItems(), writeSheet);
						page.getItems().clear();// ???????????????????????????
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Override
	public void exportSelectgoodsInTransitlist(GoodsInTransitReqDTO dto, HttpServletResponse response) {
		ExcelWriter excelWriter = null;
		try {
			dto.setPageNo(1);// ?????????1???
			dto.setPageSize(1000);// ????????????1000?????????

			ComResponse<Page<GoodsInTransit>> data = this.goodsInTransitFeign.selectgoodsInTransitlist(dto);

			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
				log.error("??????????????????????????????>>>{}", data);
				throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(), "????????????????????????????????????" + data.getMessage());
			}
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			String title = new String("??????????????????".getBytes(), StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
					URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));

			Page<GoodsInTransit> page = data.getData();
			PageParam param = page.getPageParam();

			if (param.getPageTotal() == 0) {
				log.info("????????????????????????>>>{}", param);
				excelWriter = EasyExcel.write(response.getOutputStream(), GoodsInTransit4Excel.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????

				excelWriter.write(page.getItems(), writeSheet);
			} else {

				excelWriter = EasyExcel.write(response.getOutputStream(), GoodsInTransit4Excel.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????
				List<GoodsInTransit4Excel> result = formateGoodsInTransit(page.getItems());
				excelWriter.write(result, writeSheet);
				result.clear();
				page.getItems().clear();// ???????????????????????????
				// ?????????????????????1
				if (param.getPageTotal() > 1) {
					// ??????????????????????????????
					for (int i = 2; i <= param.getPageTotal(); i++) {
						dto.setPageNo(i);
						data = this.goodsInTransitFeign.selectgoodsInTransitlist(dto);
						if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
							log.error("??????????????????????????????>>>{}", data);
							return;
						}
						page = data.getData();
						result = formateGoodsInTransit(page.getItems());
						excelWriter.write(result, writeSheet);
						result.clear();
						page.getItems().clear();// ???????????????????????????
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	public List<GoodsInTransit4Excel> formateGoodsInTransit(List<GoodsInTransit> list) {
		return list.stream().map(p -> {
			GoodsInTransit4Excel excel = new GoodsInTransit4Excel();
			excel.setProductNo(p.getProductNo());
			excel.setFinanalOwnerName(p.getFinanalOwnerName());
			excel.setProductName(p.getProductName());
			excel.setProductBarCode(p.getProductBarCode());
			excel.setSpec(p.getSpec() + p.getUnit());
			excel.setPackageUnit(p.getPackageUnit());
			excel.setBatch(p.getBatch());
			excel.setTotalCoubnt(p.getTotalCoubnt());

			return excel;
		}).collect(Collectors.toList());
	}

	@Override
	public void sexportselectOrderSaleDetail(OrderSellDetailReqDTO dto, HttpServletResponse response) {
		ExcelWriter excelWriter = null;
		try {
			dto.setPageNo(1);// ?????????1???
			ComResponse<Page<OrderSellDetailResDTO>> data = this.orderSearchClient.selectOrderSaleDetail(dto);

			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
				log.error("????????????????????????????????????>>>{}", data);
				throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),
						"??????????????????????????????????????????" + data.getMessage());
			}
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			String title = new String("????????????????????????".getBytes(), StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
					URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));

			Page<OrderSellDetailResDTO> page = data.getData();
			PageParam param = page.getPageParam();
			if (param.getPageTotal() == 0) {
				log.info("??????????????????????????????>>>{}", param);
				excelWriter = EasyExcel.write(response.getOutputStream(), OrderSellDetailResDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????
				excelWriter.write(page.getItems(), writeSheet);

			} else {
				excelWriter = EasyExcel.write(response.getOutputStream(), OrderSellDetailResDTO.class)
						.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy())
						.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy()).build();
				// ??????????????????sheet
				WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
				// ???????????????????????????????????????

				excelWriter.write(page.getItems(), writeSheet);
				page.getItems().clear();// ???????????????????????????
				// ?????????????????????1
				if (param.getPageTotal() > 1) {
					// ??????????????????????????????
					for (int i = 2; i <= param.getPageTotal(); i++) {
						dto.setPageNo(i);
						data = this.orderSearchClient.selectOrderSaleDetail(dto);
						if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
							log.error("????????????????????????????????????>>>{}", data);
							return;
						}
						page = data.getData();

						excelWriter.write(page.getItems(), writeSheet);
						page.getItems().clear();// ???????????????????????????
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param list
	 * @return
	 */
	private List<Settlement4ExportDTO> formatesettleData(List<SettlementDTO> list) {
		List<Settlement4ExportDTO> result = new ArrayList<>();
		list.forEach(m -> {
			Settlement4ExportDTO dto = new Settlement4ExportDTO();
			dto.setSettlementCode(m.getSettlementCode());
			dto.setExpressCompanyName(m.getExpressCompanyName());
			dto.setFinancialOwnerName(m.getFinancialOwnerName());
			dto.setExternalBillSn(m.getExternalBillSn());
//
			dto.setSettleTotalmoney(m.getSettleTotalmoney());
			dto.setReceiveTotalmoney(m.getReceiveTotalmoney());
			dto.setServiceFeeTotalmoney(m.getFreezeTotalmoney());
			dto.setFreezeTotalmoney(m.getFreezeTotalmoney());
			dto.setSettleStatus(m.getSettleStatus() == 0 ? "?????????" : "?????????");
			dto.setSettlementStaffName(m.getSettlementStaffName());
			dto.setSettleStartDate(DateFormatUtil.dateToString(m.getSettleStartDate(),
					CommonConstant.JSON_FORMAT_PATTERN_DATE) + "???"
					+ DateFormatUtil.dateToString(m.getSettleEndDate(), CommonConstant.JSON_FORMAT_PATTERN_DATE));
			result.add(dto);
		});
		return result;
	}
}

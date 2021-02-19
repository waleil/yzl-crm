package cn.net.yzl.crm.controller.order;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.ComparisonMgtFeignClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.order.model.excel.ExcelResult;
import cn.net.yzl.order.model.vo.order.CompareOrderIn;
import cn.net.yzl.order.model.vo.order.CompareOrderParam;
import cn.net.yzl.order.model.vo.order.CompareOrderType1Out;
import cn.net.yzl.order.model.vo.order.CompareOrderType2Out;
import cn.net.yzl.order.model.vo.order.ImportParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 对账订单管理
 * 
 * @author zhangweiwei
 * @date 2021年2月8日,上午9:35:03
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/comparisonmgt")
@Slf4j
public class ComparisonMgtController {
	private WriteHandler writeHandler = new LongestMatchColumnWidthStyleStrategy();
	@Resource
	private ComparisonMgtFeignClient comparisonMgtFeignClient;

	@PostMapping("/v1/querytype1pagelist")
	@ApiOperation(value = "查询待对账订单列表--支持分页", notes = "查询待对账订单列表--支持分页")
	public ComResponse<Page<CompareOrderType1Out>> queryType1PageList(@RequestBody CompareOrderIn orderin) {
		return this.comparisonMgtFeignClient.queryType1PageList(orderin);
	}

	@PostMapping("/v1/querytype2pagelist")
	@ApiOperation(value = "查询已对账订单列表--支持分页", notes = "查询已对账订单列表--支持分页")
	public ComResponse<Page<CompareOrderType2Out>> queryType2PageList(@RequestBody CompareOrderIn orderin) {
		return this.comparisonMgtFeignClient.queryType2PageList(orderin);
	}

	@PostMapping("/v1/exporttype1list")
	@ApiOperation(value = "导出待对账订单列表", notes = "导出待对账订单列表")
	public void exportType1List(@RequestBody CompareOrderIn orderin, HttpServletResponse response) throws Exception {
		orderin.setPageNo(1);// 默认第1页
		orderin.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<CompareOrderType1Out>> data = this.comparisonMgtFeignClient.queryType1PageList(orderin);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
			log.error("导出待对账订单列表异常>>>{}", data);
			return;
		}
		Page<CompareOrderType1Out> page = data.getData();
		PageParam param = page.getPageParam();
		if (param.getPageTotal() == 0) {
			log.info("待对账订单列表为空>>>{}", param);
			return;
		}
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "待对账订单列表";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
				URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), CompareOrderType1Out.class)
					.registerWriteHandler(this.writeHandler).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					orderin.setPageNo(i);
					data = this.comparisonMgtFeignClient.queryType1PageList(orderin);
					if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
						log.error("导出待对账订单列表异常>>>{}", data);
						return;
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
					page.getItems().clear();// 存储完成后清理集合
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@PostMapping("/v1/exporttype2list")
	@ApiOperation(value = "导出已对账订单列表", notes = "导出已对账订单列表")
	public void exportType2List(@RequestBody CompareOrderIn orderin, HttpServletResponse response) throws Exception {
		orderin.setPageNo(1);// 默认第1页
		orderin.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<CompareOrderType2Out>> data = this.comparisonMgtFeignClient.queryType2PageList(orderin);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
			log.error("导出已对账订单列表异常>>>{}", data);
			return;
		}
		Page<CompareOrderType2Out> page = data.getData();
		PageParam param = page.getPageParam();
		if (param.getPageTotal() == 0) {
			log.info("已对账订单列表为空>>>{}", param);
			return;
		}
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "已对账订单列表";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
				URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), CompareOrderType2Out.class)
					.registerWriteHandler(this.writeHandler).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					orderin.setPageNo(i);
					data = this.comparisonMgtFeignClient.queryType2PageList(orderin);
					if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
						log.error("导出已对账订单列表异常>>>{}", data);
						return;
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
					page.getItems().clear();// 存储完成后清理集合
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@GetMapping("/v1/download")
	@ApiOperation(value = "下载快递对账单模板", notes = "下载快递对账单模板")
	public ResponseEntity<byte[]> download() throws Exception {
		File file = ResourceUtils
				.getFile(String.format("%sexcel/comparisonmgt/template.xlsx", ResourceUtils.CLASSPATH_URL_PREFIX));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.builder("attachment")
				.filename(URLEncoder.encode("快递对账单导入模板.xlsx", StandardCharsets.UTF_8.name())).build());
		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(FileCopyUtils.copyToByteArray(file));
	}

	@PostMapping("/v1/import")
	@ApiOperation(value = "快递对账单导入", notes = "快递对账单导入")
	public ComResponse<ExcelResult> importFromExcel(@RequestBody ImportParam param) {
		if (!StringUtils.hasText(param.getExpressCompanyCode())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "快递公司编码不能为空");
		}
		if (!StringUtils.hasText(param.getFileUrl())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "上传文件URL不能为空");
		}
		return this.comparisonMgtFeignClient.importFromExcel(param);
	}

	@PostMapping("/v1/compare")
	@ApiOperation(value = "对账", notes = "对账")
	public ComResponse<Object> compareOrder(@RequestBody CompareOrderParam param) {
		if (!StringUtils.hasText(param.getUserNo())) {
			param.setUserNo(QueryIds.userNo.get());
		}
		if (!StringUtils.hasText(param.getUserNo())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "登录用户编码不能为空");
		}
		if (!StringUtils.hasText(param.getUserName())) {
			param.setUserName(QueryIds.userName.get());
		}
		if (!StringUtils.hasText(param.getUserName())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "登录用户姓名不能为空");
		}
		if (CollectionUtils.isEmpty(param.getExpressNums())) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "快递号不能为空");
		}
		return this.comparisonMgtFeignClient.compareOrder(param);
	}
}

package cn.net.yzl.crm.controller.order;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.ComparisonMgtFeignClient;
import cn.net.yzl.order.model.vo.order.CompareOrderIn;
import cn.net.yzl.order.model.vo.order.CompareOrderType1Out;
import cn.net.yzl.order.model.vo.order.CompareOrderType2Out;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 对账订单管理
 * 
 * @author zhangweiwei
 * @date 2021年2月8日,上午9:35:03
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/comparisonmgt")
public class ComparisonMgtController {
	private static final String FILE_NAME = "快递对账单导入模板.xlsx";
	private static final String FILE_PATH = String.format("%sexcel/%s", ResourceUtils.CLASSPATH_URL_PREFIX, FILE_NAME);
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

	@GetMapping("/v1/download")
	@ApiOperation(value = "下载快递对账单模板", notes = "下载快递对账单模板")
	public ResponseEntity<byte[]> download() throws Exception {
		File file = ResourceUtils.getFile(FILE_PATH);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.builder("attachment")
				.filename(URLEncoder.encode(FILE_NAME, StandardCharsets.UTF_8.name())).build());
		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(FileCopyUtils.copyToByteArray(file));
	}
}

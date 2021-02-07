package cn.net.yzl.crm.controller.order;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
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
import cn.net.yzl.crm.client.order.AccountDetailFeignClient;
import cn.net.yzl.order.model.vo.member.AccountDetailIn;
import cn.net.yzl.order.model.vo.member.AccountDetailOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 账户余额明细控制器
 * 
 * @author zhangweiwei
 * @date 2021年2月6日,下午7:07:21
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/accountdetail")
@Slf4j
public class AccountDetailController {
	private WriteHandler writeHandler = new LongestMatchColumnWidthStyleStrategy();
	@Resource
	private AccountDetailFeignClient accountDetailFeignClient;

	@PostMapping("/v1/querypagelist")
	@ApiOperation(value = "查询账户余额明细列表--支持分页", notes = "查询账户余额明细列表--支持分页")
	public ComResponse<Page<AccountDetailOut>> queryPageList(@RequestBody AccountDetailIn accountDetailIn) {
		return this.accountDetailFeignClient.queryPageList(accountDetailIn);
	}

	@PostMapping("/v1/export")
	@ApiOperation(value = "导出账户余额明细列表", notes = "导出账户余额明细列表")
	public void export(@RequestBody AccountDetailIn accountDetailIn, HttpServletResponse response) throws Exception {
		ComResponse<Page<AccountDetailOut>> data = this.accountDetailFeignClient.queryPageList(accountDetailIn);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
			log.error("导出账户余额明细列表异常>>>{}", data);
			return;
		}
		Page<AccountDetailOut> page = data.getData();
		PageParam param = page.getPageParam();
		if (param.getPageTotal() == 0) {
			log.info("账户余额明细列表为空>>>{}", param);
			return;
		}
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "账户余额明细";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s%s.xlsx",
				URLEncoder.encode(title, StandardCharsets.UTF_8.name()), System.currentTimeMillis()));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), AccountDetailOut.class)
					.registerWriteHandler(this.writeHandler).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					accountDetailIn.setPageNo(i);
					data = this.accountDetailFeignClient.queryPageList(accountDetailIn);
					if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
						log.error("导出账户余额明细列表异常>>>{}", data);
						return;
					}
					page = data.getData();
					excelWriter.write(page.getItems(), writeSheet);
				}
			}
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
}

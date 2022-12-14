package cn.net.yzl.crm.controller.order;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.AccountDetailFeignClient;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.ExcelStyleUtils;
import cn.net.yzl.order.model.vo.member.AccountDetailIn;
import cn.net.yzl.order.model.vo.member.AccountDetailOut;
import cn.net.yzl.order.model.vo.member.AccountDetailOut.DetailSummary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 账户余额明细控制器
 * 
 * @author zhangweiwei
 * @date 2021年2月6日,下午7:07:21
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/accountdetail")
public class AccountDetailController {
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
		// XXX 为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。
		if (accountDetailIn.getSettleTimeFrom() == null) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择对账开始时间");
		}
		if (accountDetailIn.getSettleTimeTo() == null) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "请选择对账结束时间");
		}
		if (Duration.between(accountDetailIn.getSettleTimeFrom(), accountDetailIn.getSettleTimeTo()).toDays() > 31L) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "为了保障系统性能稳定性，导出的最大时间限制为1个月，请重新选择查询范围。");
		}
		accountDetailIn.setPageNo(1);// 默认第1页
		accountDetailIn.setPageSize(1000);// 默认每页1000条数据
		ComResponse<Page<AccountDetailOut>> data = this.accountDetailFeignClient.queryPageList(accountDetailIn);
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
			throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出账户余额明细列表异常");
		}
		Page<AccountDetailOut> page = data.getData();
		PageParam param = page.getPageParam();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String title = "账户余额明细";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				String.format("attachment;filename=%s.xlsx", URLEncoder.encode(title, StandardCharsets.UTF_8.name())));
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), AccountDetailOut.class)
					.registerWriteHandler(ExcelStyleUtils.getLongestMatchColumnWidthStyleStrategy())
					.registerWriteHandler(ExcelStyleUtils.getHorizontalCellStyleStrategy()).build();
			// 写入到同一个sheet
			WriteSheet writeSheet = EasyExcel.writerSheet(title).build();
			// 此处已经获取到第一页的数据
			excelWriter.write(page.getItems(), writeSheet);
			page.getItems().clear();// 存储完成后清理集合
			// 如果总页数大于1
			if (param.getPageTotal() > 1) {
				// 直接从第二页开始获取
				for (int i = 2; i <= param.getPageTotal(); i++) {
					accountDetailIn.setPageNo(i);
					data = this.accountDetailFeignClient.queryPageList(accountDetailIn);
					if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(data.getCode())) {
						throw new BizException(ResponseCodeEnums.ERROR.getCode(), "导出账户余额明细列表异常");
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

	@PostMapping("/v1/querysummary")
	@ApiOperation(value = "查询账户余额明细列表汇总", notes = "查询账户余额明细列表汇总")
	public ComResponse<DetailSummary> querySummary(@RequestBody AccountDetailIn accountDetailIn) {
		return this.accountDetailFeignClient.querySummary(accountDetailIn);
	}
}

package cn.net.yzl.crm.test;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import cn.net.yzl.crm.controller.store.listen.InventoryExcelListener;
import cn.net.yzl.model.vo.InventoryProductVo;
import cn.net.yzl.order.model.vo.member.AccountDetailOut;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月7日,下午9:59:08
 */
public class EasyExcelTests {
	private AtomicInteger index = new AtomicInteger(0);

	private List<AccountDetailOut> queryData() {
		List<AccountDetailOut> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(new AccountDetailOut(String.format("会员卡号一二三四五六七八九十%s", index.incrementAndGet()),
					String.format("会员姓名一二三四五六七八九十%s", index.get()), LocalDateTime.now(),
					String.format("订单号一二三四五六七八九十%s", index.get()), String.format("快递公司一二三四五六七八九十%s", index.get()),
					String.format("快递号一二三四五六七八九十%s", index.get()), LocalDateTime.now(), LocalDateTime.now(),
					String.format("业务类型一二三四五六七八九十%s", index.get()), BigDecimal.valueOf(index.get()),
					BigDecimal.valueOf(index.get()), LocalDateTime.now()));
		}
		return list;
	}

	@Test
	public void testWrite() {
		// 头部的样式
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		// 头部背景颜色
		headWriteCellStyle.setFillForegroundColor((short) 41);
		// 头部字体
		WriteFont headWriteFont = new WriteFont();
		// 头部字体大小
		headWriteFont.setFontHeightInPoints((short) 14);
		headWriteCellStyle.setWriteFont(headWriteFont);
		headWriteCellStyle.setShrinkToFit(true);
		// 内容的样式
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		// 内容字体
		WriteFont contentWriteFont = new WriteFont();
		// 内容字体名称
		contentWriteFont.setFontName("Microsoft YaHei Regular");
		// 内容字体大小
		contentWriteFont.setFontHeightInPoints((short) 12);
		contentWriteCellStyle.setWriteFont(contentWriteFont);
		contentWriteCellStyle.setShrinkToFit(true);
		// 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
		HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
				contentWriteCellStyle);
		// 方法1 如果写到同一个sheet
		ExcelWriter excelWriter = null;
		try {
			// 这里 需要指定写用哪个class去写
			excelWriter = EasyExcel.write(Paths.get("d:", "test.xlsx").toFile(), AccountDetailOut.class)
					.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
					.registerWriteHandler(horizontalCellStyleStrategy).build();
			// 这里注意 如果同一个sheet只要创建一次
			WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
			// 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
			for (int i = 0; i < 5; i++) {
				// 分页去数据库查询数据 这里可以去数据库查询每一页的数据
				excelWriter.write(this.queryData(), writeSheet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 千万别忘记finish 会帮忙关闭流
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Test
	public void testReadStore() {

		// 创建监听器
		InventoryExcelListener inventoryExcelListener = new InventoryExcelListener();
		ExcelReader build = EasyExcel.read(Paths.get("D:\\PD2021-02-20 00_00_00 (3).xlsx").toFile(),
				InventoryProductVo.class, inventoryExcelListener).build();
		build.readAll();

		Map<String, String> errorMessageMap = inventoryExcelListener.getErrorMessageMap();
		System.out.println(errorMessageMap);

	}

}

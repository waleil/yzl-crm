package cn.net.yzl.crm.test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import cn.net.yzl.crm.model.order.OrderInvoice4Export;
import cn.net.yzl.order.enums.OrderLogisticsStatus;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月7日,下午9:59:08
 */
public class EasyExcelTests {
	private AtomicInteger index = new AtomicInteger(0);

	private List<OrderInvoice4Export> queryData() {
		List<OrderInvoice4Export> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(new OrderInvoice4Export().setOrderNo(String.valueOf(index.incrementAndGet()))
					.setFinancialOwnerName(String.format("FinancialOwnerName%s", index.get()))
					.setTaxMode(String.format("电子发票纸质发票%s", index.get()))
					.setStatsStr(String.format("已开票未开票%s", index.get()))
					.setPayType(String.format("货到付款款到发货%s", index.get()))
					.setLogisticsStatus(OrderLogisticsStatus.ORDER_LOGIST_STATUS_0.getName())
					.setReveiverAddress(String.format("ReveiverAddress%s", index.get())).setCreateTime(new Date())
					.setInvoiceTime(new Date()));
		}
		return list;
	}

	@Test
	public void testWrite() {
		// 方法1 如果写到同一个sheet
		ExcelWriter excelWriter = null;
		try {
			// 这里 需要指定写用哪个class去写
			excelWriter = EasyExcel.write(Paths.get("d:", "test.xlsx").toFile(), OrderInvoice4Export.class)
					.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
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
}

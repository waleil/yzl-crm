package cn.net.yzl.crm.test;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.util.ResourceUtils;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月16日,下午8:46:09
 */
@SpringBootTest
public class ComparisonMgtControllerTests {
	@Resource
	private ApplicationContext context;

	@Test
	public void testClassPathResource() {
		try {
			String name = "快递对账单导入模板.xlsx";
			System.err.println(new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
			ClassPathResource classPathResource = new ClassPathResource(String.format("excel/%s", name));
			System.err.println(classPathResource);
			System.err.println(
					ResourceUtils.getFile(String.format("%sexcel/%s", ResourceUtils.CLASSPATH_URL_PREFIX, name)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testContentDisposition() {
		try {
			System.err.println(ContentDisposition.builder("attachment")
					.filename("快递对账单导入模板.xlsx", StandardCharsets.UTF_8).build().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

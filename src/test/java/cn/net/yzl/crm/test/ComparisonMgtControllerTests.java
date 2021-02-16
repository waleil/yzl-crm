package cn.net.yzl.crm.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月16日,下午8:46:09
 */
@SpringBootTest
public class ComparisonMgtControllerTests {
	@Autowired
	private ApplicationContext context;

	@Test
	public void test() {
		try {
			System.err.println(this.context.getBeansOfType(Resource.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.client.product.MealClient;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月24日,下午1:36:24
 */
@SpringBootTest
public class MealClientTests {
	@Resource
	private MealClient mealClient;

	@Test
	public void testQueryListProductMealByCodes() {
		try {
			String codes = "T0000145,T0000142";
			this.mealClient.queryByIds(codes).getData().forEach(System.err::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

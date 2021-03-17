package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.client.store.StoreFeginService;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月17日,下午2:02:07
 */
@SpringBootTest
public class StoreFeginServiceTests {
	@Resource
	private StoreFeginService storeFeginService;

	@Test
	public void testSelectStoreListPage() {
		try {
			System.err.println(this.storeFeginService.selectStoreListPage(1, 10, null, null, null, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

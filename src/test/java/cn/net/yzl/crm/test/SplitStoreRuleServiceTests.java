package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.service.SplitStoreRuleService;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年3月11日,下午6:13:44
 */
@SpringBootTest
public class SplitStoreRuleServiceTests {
	@Resource
	private SplitStoreRuleService splitStoreRuleService;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testGetSplitStoreRuleList() {
		try {
			System.err.println(this.objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(this.splitStoreRuleService.getSplitStoreRuleList(1, 10)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

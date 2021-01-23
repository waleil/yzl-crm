package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.order.enums.RedisKeys;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月18日,下午8:29:34
 */
@SpringBootTest
public class RedisTemplateTests {
	@Resource
	private RedisUtil redisUtil;

	@Test
	public void testGetSeqNo() {
		for (int i = 0; i < 10; i++) {
			// 订单号生成器
			System.err.println(redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, "100000", "100000",
					RedisKeys.CREATE_ORDER_NO, 4));
		}
		for (int i = 0; i < 10; i++) {
			// 售后单号生成器
			System.err.println(
					redisUtil.getSeqNo(RedisKeys.SALE_ORDER_NO_PREFIX, "100000", "100000", RedisKeys.SALE_ORDER_NO, 4));
		}
		for (int i = 0; i < 10; i++) {
			// 拒收单号生成器
			System.err.println(redisUtil.getSeqNo(RedisKeys.REJECT_ORDER_NO_PREFIX, "100000", "100000",
					RedisKeys.SALE_ORDER_NO, 4));
		}
	}
}

package cn.net.yzl.crm;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * 单元测试类
 * @author zhangweiwei
 * @date 2021年1月18日,下午7:10:59
 */
public class SimpleTests {
	@Test
	public void testCollect() {
		System.err.println(Arrays.asList("a", "b", "c").stream()
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString());
		System.err.println(Arrays.asList("a", "b", "c").stream().collect(Collectors.joining(",")));
	}
}

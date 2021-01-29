package cn.net.yzl.crm.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.dao.RequestMessageMapper;
import cn.net.yzl.crm.model.RequestMessage;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年1月29日,下午6:48:47
 */
@SpringBootTest
public class RequestMessageMapperTests {
	@Resource
	private RequestMessageMapper requestMessageMapper;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	public void testInsert() {
		try {
			RequestMessage message = new RequestMessage();
			message.setBusCode("11");
			message.setCallBackUrl("12");
			message.setCreateCode("13");
			message.setMessageCode("14");
			message.setRequestParam(this.objectMapper.writeValueAsString(message));
			message.setRequestUrl("16");
			message.setUpdateCode("18");
			System.err.println(this.requestMessageMapper.insert(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

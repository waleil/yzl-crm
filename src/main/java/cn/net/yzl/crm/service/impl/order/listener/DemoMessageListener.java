package cn.net.yzl.crm.service.impl.order.listener;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class DemoMessageListener implements ChannelAwareMessageListener {

	@Override
	@RabbitListener(queues = "test.queue")
	public void onMessage(Message message, Channel channel) throws Exception {
		try {
			System.err.println(new String(message.getBody(), StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

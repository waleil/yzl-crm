package cn.net.yzl.crm;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.common.swagger2.EnableSwagger;
import lombok.extern.slf4j.Slf4j;

@EnableSwagger
@SpringBootApplication(scanBasePackages = { "cn.net.yzl.crm", "cn.net.yzl.logger", "cn.net.yzl.pm" })
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "cn.net.yzl.crm.service.micservice", "cn.net.yzl.crm.client" })
@ServletComponentScan(basePackages = { "cn.net.yzl.crm.filters" })
@Slf4j
public class YzlCrmApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(YzlCrmApplication.class, args);
	}

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Override
	public void run(String... args) throws Exception {
		// 设置消息发布确认回调
		this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				log.info("消息到达RabbitMQ服务器");
			} else {
				log.error("消息[{}]未到达RabbitMQ服务器[原因：{}]", correlationData, cause);
			}
		});
	}

	/**
	 * 定义消息转换器，使用json进行转换
	 *
	 * @param objectMapper jackson转换器
	 * @return 定义消息转换器，使用json进行转换
	 * @author chengyu
	 * @date 2021年1月30日,上午11:34:02
	 */
	@Bean
	public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}

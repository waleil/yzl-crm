package cn.net.yzl.crm.bean;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//@Configuration
@Slf4j
//@AutoConfigureAfter(value = RabbitAutoConfiguration.class)
public class RabbitConfig  {
    @Autowired
    private RabbitTemplate rabbitTemplate;


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

    @PostConstruct
    public void init()  {
        // 设置消息发布确认回调
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息到达RabbitMQ服务器");
            } else {
                log.error("消息[{}]未到达RabbitMQ服务器[原因：{}]", correlationData, cause);
            }
        });

    }
}

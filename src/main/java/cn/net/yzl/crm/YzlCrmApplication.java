package cn.net.yzl.crm;

import cn.net.yzl.common.swagger2.EnableSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableSwagger
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.net.yzl.crm.service.*"})
@MapperScan("cn.net.yzl.crm.dao")
public class YzlCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(YzlCrmApplication.class, args);
    }

}

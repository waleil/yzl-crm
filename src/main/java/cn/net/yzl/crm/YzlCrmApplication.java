package cn.net.yzl.crm;

import cn.net.yzl.common.swagger2.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger
@SpringBootApplication(scanBasePackages = {"cn.net.yzl.crm","cn.net.yzl.logger","cn.net.yzl.pm"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.net.yzl.crm.service.micservice","cn.net.yzl.crm.client"})
public class YzlCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(YzlCrmApplication.class, args);
    }

}

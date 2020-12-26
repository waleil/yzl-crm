package cn.net.yzl.crm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 *  @author: wangxiao
 *  @date: ${DATE} ${TIME}
 *  @desc: 获取nacos 配置 的值
 **/

@Data
@Component
@RefreshScope
public class NacosValue {

//    @Value("${server.gateway.url:noCheck}")
//    private String serverGatewayUrl;

    @Value("${appId:}")
    private String appId;
}

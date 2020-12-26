package cn.net.yzl.crm.config;

import cn.net.yzl.common.util.UUIDGenerator;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 填充 feign 的 header
 */
@Slf4j
@Configuration
public class FeignHeadConfiguration {

    private final NacosValue nacosValue;

    @Autowired
    public FeignHeadConfiguration(NacosValue nacosValue) {
        this.nacosValue = nacosValue;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                requestTemplate.header("appId", nacosValue.getAppId());

                //将traceId和spanId添加到新的请求头中转发到下游服务
                requestTemplate.header("traceId", QueryIds.tranceId.get());

                String appName = requestTemplate.feignTarget().name();
                String cSpanId = UUIDGenerator.getUUID() + ":" + appName;
                requestTemplate.header("spanId", cSpanId);


                log.info("crm request:[{}], traceId:[{}], spanId:[{}], cSpanId:{}", appName, QueryIds.tranceId.get(), QueryIds.spanId.get(), cSpanId);

            }
        };
    }

}
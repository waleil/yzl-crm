package cn.net.yzl.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import cn.net.yzl.common.util.UUIDGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 填充 feign 的 header
 */
@Slf4j
@Configuration
public class FeignHeadConfiguration implements RequestInterceptor {

    private final NacosValue nacosValue;

    @Autowired
    public FeignHeadConfiguration(NacosValue nacosValue) {
        this.nacosValue = nacosValue;
    }


	@Override
	public void apply(RequestTemplate template) {
		template.header("appId", nacosValue.getAppId());
		
		//将traceId和spanId添加到新的请求头中转发到下游服务
		template.header("traceId", QueryIds.tranceId.get());
		
		String appName = template.feignTarget().name();
		String cSpanId = UUIDGenerator.getUUID() + ":" + appName;
		template.header("spanId", cSpanId);
		
		
		log.info("crm request:[{}], traceId:[{}], spanId:[{}], cSpanId:{}", appName, QueryIds.tranceId.get(), QueryIds.spanId.get(), cSpanId);
	}

}
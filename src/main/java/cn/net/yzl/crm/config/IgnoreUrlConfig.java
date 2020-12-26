package cn.net.yzl.crm.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Configuration
@RefreshScope //实时刷新nacos配置中心文件
@ConfigurationProperties(prefix = "ignore")
public class IgnoreUrlConfig {
    private String urls;
    private Set<String> urlsSet;

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
        if(StringUtils.isNotEmpty(urls)){
            setUrlsSet(org.springframework.util.StringUtils.commaDelimitedListToSet(urls));
        }else{
            setUrlsSet(Collections.emptySet());
        }
    }

    public Set<String> getUrlsSet() {
        return urlsSet;
    }

    private void setUrlsSet(Set<String> urlsSet) {
        this.urlsSet = urlsSet;
    }
}

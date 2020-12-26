package cn.net.yzl.crm.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Configuration
@RefreshScope //实时刷新nacos配置中心文件
@ConfigurationProperties(prefix = "file")
public class FileFormatConfig {

    private String format;
    private Set<String> formatSet;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        if(StringUtils.isNotEmpty(format)){
            setFormatSet(org.springframework.util.StringUtils.commaDelimitedListToSet(format));
        }else{
            setFormatSet(Collections.emptySet());
        }
    }

    public Set<String> getFormatSet() {
        return formatSet;
    }

    private void setFormatSet(Set<String> formatSet) {
        this.formatSet = formatSet;
    }
}

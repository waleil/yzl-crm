package cn.net.yzl.crm.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author lichanghong
 * @version 1.0
 * @title: FastDFSConfig
 * @description fastDFS配置文件
 * @date: 2020/12/30 7:04 下午
 */
@Configuration
@RefreshScope //实时刷新nacos配置中心文件
@ConfigurationProperties(prefix = "fast-dfs")
public class FastDFSConfig {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

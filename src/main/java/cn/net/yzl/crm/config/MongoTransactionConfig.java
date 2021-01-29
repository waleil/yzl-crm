package cn.net.yzl.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * @Description mongo事务配置
 * @Author jingweitao
 * @Date 14:15 2020/12/10
 **/
@Configuration
public class MongoTransactionConfig {

  /*  @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }*/

}
//package cn.net.yzl.crm.api.bean;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//@MapperScan(basePackages = "cn.net.yzl.crm.api.mapper.crmwrite", sqlSessionTemplateRef  = "crmwriteSqlSessionTemplate")
//public class CrmWriteConfig {
//    @Bean(name = "crmwriteDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.crmwrite")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "crmwriteTransactionManager")
//    public DataSourceTransactionManager transactionManager(@Qualifier("crmwriteDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "crmwriteSqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("crmwriteDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:cn/net/yzl/crm/api/mapper/crmwrite/*.xml"));
//        return factoryBean.getObject();
//    }
//
//    @Bean(name = "crmwriteSqlSessionTemplate")
//    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("crmwriteSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//}

//package cn.net.yzl.crm.crmapi.bean;
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
//@Configuration
//@MapperScan(basePackages = "cn.net.yzl.crm.api.mapper.crmread", sqlSessionTemplateRef  = "crmreadSqlSessionTemplate")
//public class CrmReadConfig {
//    @Bean(name = "crmreadDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.crmread")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "crmreadTransactionManager")
//    public DataSourceTransactionManager transactionManager(@Qualifier("crmreadDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "crmreadSqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("crmreadDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:cn/net/yzl/crm/api/mapper/crmread/*.xml"));
//        return factoryBean.getObject();
//    }
//
//    @Bean(name = "crmreadSqlSessionTemplate")
//    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("crmreadSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}

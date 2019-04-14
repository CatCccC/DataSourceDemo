package com.catcccc.datasourcedemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {
    "com.catcccc.datasourcedemo.dao.db1"}, sqlSessionFactoryRef = "db1SqlSessionFactory")
public class DB1DataSourceConfig {

  private static final String MAPPER_LOCATION = "classpath:mybatis/db1/**/*.xml";

  private final MybatisProperties myBatisProperties;

  public DB1DataSourceConfig(
      MybatisProperties myBatisProperties) {
    this.myBatisProperties = myBatisProperties;
  }

  @Bean(name = "db1DataSource", initMethod = "init", destroyMethod = "close")
  @ConfigurationProperties("spring.datasource.druid.db1")
  public DruidDataSource db1DataSource() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean(name = "db1TransactionManager")
  public DataSourceTransactionManager db1TransactionManager(
      @Qualifier("db1DataSource") DataSource db1DataSource) {
    return new DataSourceTransactionManager(db1DataSource);
  }

  @Bean(name = "db1SqlSessionFactory")
  public SqlSessionFactory db1SqlSessionFactory(
      @Qualifier("db1DataSource") DataSource db1DataSource)
      throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(db1DataSource);
    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(DB1DataSourceConfig.MAPPER_LOCATION));
    //sessionFactory.setTypeAliasesPackage("com.xx.po");
    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session
        .Configuration();
    if (myBatisProperties.getConfiguration() != null) {
      BeanUtils.copyProperties(myBatisProperties.getConfiguration(), configuration);
    }
    sessionFactory.setConfiguration(configuration);
    return sessionFactory.getObject();
  }

}
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// 扫描 Mapper 接口并容器管理
@EnableTransactionManagement
@MapperScan(basePackages = {
    "com.catcccc.datasourcedemo.dao.db2"}, sqlSessionFactoryRef = "db2SqlSessionFactory")
public class DB2DataSourceConfig {

  // 精确到 db2 目录，以便跟其他数据源隔离
  private static final String MAPPER_LOCATION = "classpath:mybatis/db2/**/*.xml";

  private final MybatisProperties myBatisProperties;

  public DB2DataSourceConfig(
      MybatisProperties myBatisProperties) {
    this.myBatisProperties = myBatisProperties;
  }
  @Primary
  @Bean(name = "db2DataSource", initMethod = "init", destroyMethod = "close")
  @ConfigurationProperties("spring.datasource.druid.db2")
  public DruidDataSource db2DataSource() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean(name = "db2TransactionManager")
  @Primary
  public DataSourceTransactionManager db2TransactionManager(
      @Qualifier("db2DataSource") DataSource masterDataSource) {
    return new DataSourceTransactionManager(masterDataSource);
  }

  @Bean(name = "db2SqlSessionFactory")
  @Primary
  public SqlSessionFactory db2SqlSessionFactory(
      @Qualifier("db2DataSource") DataSource db2DataSource)
      throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(db2DataSource);
    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(DB2DataSourceConfig.MAPPER_LOCATION));
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
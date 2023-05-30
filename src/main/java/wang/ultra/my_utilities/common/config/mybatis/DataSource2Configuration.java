package wang.ultra.my_utilities.common.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "wang.ultra.my_utilities.idiompedia.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory2",
        sqlSessionTemplateRef = "sqlSessionTemplate2")
public class DataSource2Configuration {
     private final DataSource dataSource;

     public DataSource2Configuration(@Qualifier("dataSource2") DataSource dataSource2) {
        this.dataSource = dataSource2;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory2() throws Exception {
         SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
         sqlSessionFactoryBean.setDataSource(dataSource);
         return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
         return new SqlSessionTemplate(sqlSessionFactory2());
    }
}

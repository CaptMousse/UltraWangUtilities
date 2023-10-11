package wang.ultra.my_utilities.common.config.datasources;

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
        basePackages = "wang.ultra.my_utilities.common.scheduler.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory7",
        sqlSessionTemplateRef = "sqlSessionTemplate7")
public class DataSource7Configuration {
    private final DataSource dataSource7;

    public DataSource7Configuration(@Qualifier("dataSource7") DataSource dataSource7) {
        this.dataSource7 = dataSource7;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory7() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource7);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate7() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory7());
    }
}

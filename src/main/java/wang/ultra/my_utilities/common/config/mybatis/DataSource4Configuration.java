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
        basePackages = "wang.ultra.my_utilities.common.download.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory4",
        sqlSessionTemplateRef = "sqlSessionTemplate4")
public class DataSource4Configuration {
    private final DataSource dataSource4;

    public DataSource4Configuration(@Qualifier("dataSource4") DataSource dataSource4) {
        this.dataSource4 = dataSource4;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory4() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource4);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate4() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory4());
    }
}

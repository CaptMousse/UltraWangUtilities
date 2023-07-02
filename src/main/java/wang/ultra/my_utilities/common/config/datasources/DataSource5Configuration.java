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
        basePackages = "wang.ultra.my_utilities.blog.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory5",
        sqlSessionTemplateRef = "sqlSessionTemplate5")
public class DataSource5Configuration {
    private final DataSource dataSource5;

    public DataSource5Configuration(@Qualifier("dataSource5") DataSource dataSource5) {
        this.dataSource5 = dataSource5;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory5() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource5);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate5() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory5());
    }
}

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
        basePackages = "wang.ultra.my_utilities.stock_exchange.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory6",
        sqlSessionTemplateRef = "sqlSessionTemplate6")
public class DataSource6Configuration {
    private final DataSource dataSource6;

    public DataSource6Configuration(@Qualifier("dataSource8") DataSource dataSource8) {
        this.dataSource6 = dataSource8;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory6() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource6);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate6() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory6());
    }
}

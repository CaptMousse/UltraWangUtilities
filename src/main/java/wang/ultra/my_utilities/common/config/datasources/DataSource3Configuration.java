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
        basePackages = "wang.ultra.my_utilities.common.monitor.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory3",
        sqlSessionTemplateRef = "sqlSessionTemplate3")
public class DataSource3Configuration {
    private final DataSource dataSource3;

    public DataSource3Configuration(@Qualifier("dataSource3") DataSource dataSource3) {
        this.dataSource3 = dataSource3;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory3() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource3);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate3() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory3());
    }
}

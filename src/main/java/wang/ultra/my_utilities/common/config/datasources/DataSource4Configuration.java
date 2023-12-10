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
        basePackages = {"wang.ultra.my_utilities.common",
                "wang.ultra.my_utilities.blog.mapper",
                "wang.ultra.my_utilities.stock_exchange.mapper",
                "wang.ultra.my_utilities.vehicle_info.mapper",
                "wang.ultra.my_utilities.attached_storage.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory",
        sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSource4Configuration {
    private final DataSource dataSource4;

    public DataSource4Configuration(@Qualifier("dataSource8") DataSource dataSource8) {
        this.dataSource4 = dataSource8;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource4);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }
}

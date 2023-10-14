package wang.ultra.my_utilities.common.config.datasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateDataSourceConfig {
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource8") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate (@Qualifier("dataSource8")DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    // 第二个数据源
//    @Bean(name = "jdbcTemplate9")
//    public JdbcTemplate jdbcTemplate9(@Qualifier("dataSource9") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}

package wang.ultra.my_utilities.common.config.datasources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.zbhdscheduler")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.idiompedia")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ultra-wang-utilities")
    public DataSource dataSource8() {
        return DataSourceBuilder.create().build();
    }
}

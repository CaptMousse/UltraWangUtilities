package wang.ultra.my_utilities.idiompedia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("wang.ultra.idiompedia.mapper")
public class IdiompediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdiompediaApplication.class, args);
	}

}

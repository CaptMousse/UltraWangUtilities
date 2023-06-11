package wang.ultra.my_utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.schedules.CommonScheduler;

@SpringBootApplication
@ServletComponentScan
public class UtilitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilitiesApplication.class, args);

		// 读取配置文件
		ConstantFromFile.setConstFromMap();

		CommonScheduler commonScheduler = new CommonScheduler();
		commonScheduler.hardwareMonitorSchedule();
	}

}

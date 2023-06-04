package wang.ultra.my_utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.aliyun_ddns_update.thread.DdnsMonitorThread;
import wang.ultra.my_utilities.common.filter.CurrentLimitingFilter;
import wang.ultra.my_utilities.hardware_monitor.thread.HardwareMonitorThread;

//@MapperScan("wang.ultra.utilities.zbhd_scheduler.mapper")
@SpringBootApplication
@ServletComponentScan
public class UtilitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilitiesApplication.class, args);

		CurrentLimitingFilter currentLimitingFilter = new CurrentLimitingFilter();
		currentLimitingFilter.currentLimitingMonitorThreadStart();

		// 读取配置文件
		ConstantFromFile.setConstFromMap();

		// 启动DDNS监控
		DdnsMonitorThread ddnsMonitorThread = new DdnsMonitorThread();
		ddnsMonitorThread.ddnsMonitorThreadStart();

		// 启动硬件监控
		HardwareMonitorThread hardwareMonitorThread = new HardwareMonitorThread();
		hardwareMonitorThread.hardwareMonitorThreadStart();
	}

}

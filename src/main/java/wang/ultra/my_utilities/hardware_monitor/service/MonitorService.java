package wang.ultra.my_utilities.hardware_monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;
import wang.ultra.my_utilities.common.monitor.service.HardwareMonitorService;
import wang.ultra.my_utilities.common.utils.SpringUtil;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.util.HashMap;
import java.util.Map;

public class MonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorService.class);

    HardwareUsageService hardwareUsageService = new HardwareUsageService();

    SendMailService sendMailService = new SendMailService();

    Double memoryUsagePrevious = hardwareUsageService.getMemoryUsage();

    Double monitorCpuUsage = ConstantFromFile.getMonitorCpuUsage();
    Double monitorCpuTemperature = ConstantFromFile.getMonitorCpuTemperature();
    Double monitorMemoryChange = ConstantFromFile.getMonitorMemoryChange();

    public void monitor() {

        // CPU用量
        Double cpuUsage = hardwareUsageService.getCpuUsage();
        String cpuUsageStr = String.format("%.2f", cpuUsage) + "%";

        // CPU温度
        Double cpuTemperature = hardwareUsageService.getCpuTemperature();
        String cpuTemperatureStr = String.format("%.2f", cpuTemperature) + "°C";

        // 内存用量
        Double memoryUsage = hardwareUsageService.getMemoryUsage();
        String memoryUsageStr = String.format("%.2f", memoryUsage) + "%";
        double memoryChange = memoryUsage - memoryUsagePrevious;
        String memoryChangeStr = String.format("%.2f", memoryChange) + "%";
        memoryUsagePrevious = memoryUsage;

        System.out.println("硬件监控时间 = " + DateConverter.getTime());
        System.out.println("CPU用量 = " + cpuUsageStr);
        System.out.println("CPU温度 = " + cpuTemperatureStr);
        System.out.println("内存变化 = " + memoryChangeStr);
        System.out.println("内存用量 = " + memoryUsageStr);
        System.out.println("\n");


        // 硬件监控发邮件报警
        if (cpuUsage >= monitorCpuUsage) {
            sendMailService.sendCpuUsageMail(cpuUsageStr);
        }
        if (cpuTemperature >= monitorCpuTemperature) {
            sendMailService.sendCpuTemperatureMail(cpuTemperatureStr);

        }
        if (Math.abs(memoryChange) >= monitorMemoryChange) {
            Map<String, String> memoryMap = new HashMap<>();
            memoryMap.put("memoryChange", memoryChangeStr);
            memoryMap.put("memoryUsage", memoryUsageStr);
            // 发送邮件
            sendMailService.sendMemoryMail(memoryMap);

        }



        // 硬件监控持久化到数据库
        MonitorEntity entity = new MonitorEntity();
        entity.setCpuUsage(cpuUsageStr);
        entity.setCpuTemperature(cpuTemperatureStr);
        entity.setMemoryUsage(memoryUsageStr);
        HardwareMonitorService hardwareMonitorService = SpringUtil.getBean(HardwareMonitorService.class);
        hardwareMonitorService.hardwareMonitorRecord(entity);
    }

}

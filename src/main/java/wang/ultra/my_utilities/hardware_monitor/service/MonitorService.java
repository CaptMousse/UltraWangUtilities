package wang.ultra.my_utilities.hardware_monitor.service;

import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;
import wang.ultra.my_utilities.common.monitor.service.HardwareMonitorService;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控
 */
public class MonitorService {


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

        // 硬件监控持久化
        MonitorEntity entity = new MonitorEntity();
        entity.setCpuUsage(cpuUsage.toString());
        entity.setCpuTemperature(cpuTemperature.toString());
        entity.setMemoryUsage(memoryUsage.toString());
        HardwareMonitorService hardwareMonitorService = SpringBeanUtils.getBean(HardwareMonitorService.class);
        hardwareMonitorService.hardwareMonitorRecord(entity);
    }

}

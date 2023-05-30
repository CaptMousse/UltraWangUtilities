package wang.ultra.my_utilities.hardware_monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.util.HashMap;
import java.util.Map;

public class MonitorService {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorService.class);

    HardwareUsageService hardwareUsageService = new HardwareUsageService();

    SendMailService sendMailService = new SendMailService();

    public void loop() {

        // 获取监听状态 0 - 停止, 1 - 运行
        String monitorStatus = ConstantFromFile.getHardwareMonitorStatus();

        // 获取内存前值
        Double memoryAvailablePrevious = hardwareUsageService.getMemoryUsage();

        LOG.info("HardwareMonitorService Started!");
        while (monitorStatus.equals("1")) {
            System.out.println("\nMonitor Hardware time = " + DateConverter.getTime());

            // 监听CPU使用率
            Double monitorCpuUsage = ConstantFromFile.getMonitorCpuUsage();
            monitorCpuUsage(monitorCpuUsage);

            // 监听CPU温度
            Double monitorCpuTemperature = ConstantFromFile.getMonitorCpuTemperature();
            monitorCpuTemperature(monitorCpuTemperature);

            // 监听内存变动
            Double monitorMemoryChange = ConstantFromFile.getMonitorMemoryChange();
            memoryAvailablePrevious = monitorMemory(monitorMemoryChange, memoryAvailablePrevious);


            try {
                Thread.sleep(ConstantFromFile.getMonitorRate());
//                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            monitorStatus = ConstantFromFile.getHardwareMonitorStatus();

        }

        System.out.println("Monitor Status = " + monitorStatus);
    }

    /**
     * 监听CPU用量
     * @param monitorCpuUsage
     */
    private void monitorCpuUsage(Double monitorCpuUsage) {
        Double cpuUsage = hardwareUsageService.getCpuUsage();
        String cpuUsageStr = String.format("%.2f", cpuUsage) + "%";
        System.out.println("cpuUsage = " + cpuUsageStr);

        if (cpuUsage >= monitorCpuUsage) {
            sendMailService.sendCpuUsageMail(cpuUsageStr);
        }
    }

    /**
     * 监听CPU温度
     *
     * @param monitorCpuTemperature
     */
    private void monitorCpuTemperature(Double monitorCpuTemperature) {
        Double cpuTemperature = hardwareUsageService.getCpuTemperature();
        String cpuTemperatureStr = String.format("%.2f", hardwareUsageService.getCpuTemperature()) + "°C";
        System.out.println("cpuTemperature = " + cpuTemperatureStr);
        if (cpuTemperature >= monitorCpuTemperature) {
            sendMailService.sendCpuTemperatureMail(cpuTemperatureStr);
        }
    }



    /**
     * 比对内存现值和前值
     *
     * @param monitorMemoryChange
     * @param memoryAvailablePrevious
     * @return
     */
    private Double monitorMemory(Double monitorMemoryChange, Double memoryAvailablePrevious) {
        Double memoryAvailableNow = hardwareUsageService.getMemoryUsage();

        double memoryChange = memoryAvailableNow - memoryAvailablePrevious;
        String memoryAvailableNowStr = String.format("%.2f", memoryAvailableNow) + "%";
        String memoryChangeStr = String.format("%.2f", memoryChange) + "%";
        System.out.println("memoryAvailableNow = " + memoryAvailableNowStr);

        if (Math.abs(memoryChange) >= monitorMemoryChange) {
            Map<String, String> memoryMap = new HashMap<>();
            memoryMap.put("memoryChange", memoryChangeStr);
            memoryMap.put("memoryAvailableNow", memoryAvailableNowStr);
            // 发送邮件
            sendMailService.sendMemoryMail(memoryMap);

        }
        memoryAvailablePrevious = memoryAvailableNow;
        return memoryAvailablePrevious;
    }
}

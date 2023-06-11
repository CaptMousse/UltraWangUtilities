package wang.ultra.my_utilities.hardware_monitor.service;



import wang.ultra.my_utilities.hardware_monitor.utils.HardwareUsageUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HardwareUsageService {
    HardwareUsageUtils hardwareUsageUtils = new HardwareUsageUtils();

    public List<String> getAllUsage() {

//        HardwareUsageUtils hardwareUsageUtils = new HardwareUsageUtils();

        Double cpuUsage = hardwareUsageUtils.getCpuUsage();
        Double cpuTemperature = hardwareUsageUtils.getCPUTemperature();
        Double memoryUsage = hardwareUsageUtils.getMemoryUsage();
        Double diskUsage = hardwareUsageUtils.getDiskUsage();

        List<String> usageList = new ArrayList<>();
        usageList.add(String.format("%.2f", cpuUsage));
        usageList.add(String.format("%.2f", cpuTemperature));
        usageList.add(String.format("%.2f", memoryUsage));
        usageList.add(String.format("%.2f", diskUsage));

        return usageList;
    }

    public Double getCpuUsage() {
        return hardwareUsageUtils.getCpuUsage();
    }

    public Double getCpuTemperature() {
//        HardwareUsageUtils hardwareUsageUtils = new HardwareUsageUtils();
        return hardwareUsageUtils.getCPUTemperature();
    }

    public Double getMemoryUsage() {
        return hardwareUsageUtils.getMemoryUsage();
    }

    /**
     * 转换成KB, MB, GB这样的单位
     *
     * @param byteNumber
     * @return
     */
    private String formatByte(Long byteNumber) {
        Double FORMAT = 1000.0;
        if (System.getProperty("os.name").contains("Windows")) {
            // 如果是Win系统就是1024
            FORMAT = 1024.0;
        }

        Double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }

        Double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }

        Double gbNumber = mbNumber / FORMAT;
        return new DecimalFormat("#.##GB").format(gbNumber);
    }

    /**
     * Double转换成百分比
     *
     * @param d
     * @return
     */
    private String formatPercentage(Double d) {
        return new DecimalFormat("#.##%").format(d);
    }
}

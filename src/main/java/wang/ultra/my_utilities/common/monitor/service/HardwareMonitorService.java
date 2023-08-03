package wang.ultra.my_utilities.common.monitor.service;

import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("hardwareMonitorService")
public class HardwareMonitorService {

    List<Map<String, String>> hourList = new ArrayList<>();
    List<Map<String, String>> minuteList = new ArrayList<>();

    String minuteRecord = "-1";

    public void hardwareMonitorRecord(MonitorEntity entity) {

        Map<String, String> entityMap = new HashMap<>();
        entityMap.put("cpuUsage", roundHalfUp(entity.getCpuUsage()));
        entityMap.put("cpuTemperature", entity.getCpuTemperature());
        entityMap.put("memoryUsage", entity.getMemoryUsage());
        minuteList.add(entityMap);

        String minute = DateConverter.getNoSymbolTime().substring(10, 12);
        if (!minute.equals(minuteRecord)) {
            // 把每个遍历出来算平均数
            List<String> cpuUsageList = new ArrayList<>();
            List<String> cpuTemperatureList = new ArrayList<>();
            List<String> memoryUsageList = new ArrayList<>();
            for (Map<String, String> map : minuteList) {
                cpuUsageList.add(map.get("cpuUsage"));
                cpuTemperatureList.add(map.get("cpuTemperature"));
                memoryUsageList.add(map.get("memoryUsage"));
            }
            double cpuUsageAvg = getAvgResultFromList(cpuUsageList);
            double cpuTemperatureAvg = getAvgResultFromList(cpuTemperatureList);
            double memoryUsageAvg = getAvgResultFromList(memoryUsageList);

            // 放进小时list里面
            Map<String, String> minuteMap = new HashMap<>();
            minuteMap.put("time", DateConverter.getNoSymbolHourMinutes());
            minuteMap.put("cpuUsage", String.valueOf(cpuUsageAvg));
            minuteMap.put("cpuTemperature", String.valueOf(cpuTemperatureAvg));
            minuteMap.put("memoryUsage", String.valueOf(memoryUsageAvg));
            hourList.add(minuteMap);

            // 初始化
            minuteRecord = minute;
            minuteList.clear();
            if (hourList.size() >= 60) {
                hourList.remove(0);
            }
        }

        System.out.println("minuteList = " + minuteList);
        System.out.println("hourList = " + hourList);
    }

    private double getAvgResultFromList(List<String> list) {
        double result = 0;
        for (String resultStr : list) {
            result += Double.parseDouble(resultStr);
        }
        return result / list.size();
    }

    private String roundHalfUp(String num) {
        BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }


    public List<Map<String, String>> showHardwareMonitorInHour() {
        return hourList;
    }
    public List<Map<String, String>> showHardwareMonitorInMinute() {
        return minuteList;
    }
}

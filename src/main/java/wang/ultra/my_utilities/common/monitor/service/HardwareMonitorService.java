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

    String minuteRecord = DateConverter.getNoSymbolTime().substring(8, 12);

    public void hardwareMonitorRecord(MonitorEntity entity) {

        Map<String, String> entityMap = new HashMap<>();
        entityMap.put("cpuUsage", roundHalfUp(entity.getCpuUsage()));
        entityMap.put("cpuTemperature", entity.getCpuTemperature());
        entityMap.put("memoryUsage", entity.getMemoryUsage());
        minuteList.add(entityMap);

        String minute = DateConverter.getNoSymbolTime().substring(8, 12);
        if (!minute.equals(minuteRecord)) {
            if (!minuteList.isEmpty()) {
                // 把每个遍历出来算平均数
                List<String> cpuUsageList = new ArrayList<>();
                List<String> cpuTemperatureList = new ArrayList<>();
                List<String> memoryUsageList = new ArrayList<>();
                for (Map<String, String> map : minuteList) {
                    cpuUsageList.add(map.get("cpuUsage"));
                    cpuTemperatureList.add(map.get("cpuTemperature"));
                    memoryUsageList.add(map.get("memoryUsage"));
                }
                String cpuUsageAvg = getAvgResultFromList(cpuUsageList);
                String cpuTemperatureAvg = getAvgResultFromList(cpuTemperatureList);
                String memoryUsageAvg = getAvgResultFromList(memoryUsageList);

                // 放进小时list里面
                Map<String, String> minuteMap = new HashMap<>();
                minuteMap.put("time", minuteRecord);    // 用前一分钟的时间
                minuteMap.put("cpuUsage", cpuUsageAvg);
                minuteMap.put("cpuTemperature", cpuTemperatureAvg);
                minuteMap.put("memoryUsage", memoryUsageAvg);
                hourList.add(minuteMap);

                // 初始化
                minuteRecord = minute;
                minuteList.clear();
                if (hourList.size() > 60) {
                    hourList.remove(0);
                }
            }
        }

        // System.out.println("minuteList = " + minuteList);
        // System.out.println("hourList = " + hourList);
    }

    private String getAvgResultFromList(List<String> list) {
        double result = 0;
        for (String resultStr : list) {
            result += Double.parseDouble(resultStr);
        }

        // String.format("%.2f", result / list.size()) + "%"

        return String.format("%.2f", result / list.size());
    }

    private String roundHalfUp(String num) {
        BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public List<Map<String, String>> getHourList() {
        return hourList;
    }

    public List<Map<String, String>> getMinuList() {
        return minuteList;
    }
}

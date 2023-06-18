package wang.ultra.my_utilities.common.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;
import wang.ultra.my_utilities.common.monitor.mapper.MonitorMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("hardwareMonitorService")
public class HardwareMonitorService {

    @Autowired
    MonitorMapper monitorMapper;

    List<Map<String, String>> daoList = new ArrayList<>();

    public void hardwareMonitorRecord(MonitorEntity entity) {
//        getBasicInfo(entity);
//        entity.setType(1);


        if (daoList.size() >= 60) {
            daoList.remove(0);
        }

        String cpuUsageHalfUp = roundHalfUp(entity.getCpuUsage());

        Map<String, String> entityMap = new HashMap<>();
        entityMap.put("time", DateConverter.getNoSymbolHourMinutes());
        entityMap.put("cpuUsage", cpuUsageHalfUp);
        entityMap.put("cpuTemperature", entity.getCpuTemperature());
        entityMap.put("memoryUsage", entity.getMemoryUsage());
        daoList.add(entityMap);

        System.out.println("daoList.size() = " + daoList.size());



//        List<MonitorEntity> entityList = new ArrayList<>();
//        entityList.add(entity);
//
//        monitorMapper.addMonitorInfo(entityList);
//        monitorMapper.addHardwareCpuUsage(entityList);
//        monitorMapper.addHardwareCpuTemperature(entityList);
//        monitorMapper.addHardwareMemoryUsage(entityList);
    }

    private String roundHalfUp(String num) {
        BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    private void getBasicInfo(MonitorEntity entity) {
        entity.setId(StringUtils.getMyUUID());
        entity.setRecord_time(DateConverter.getNoSymbolTime());
        if (ConstantFromFile.getHostname().equals("test")) {
            // test代表这是开发环境
            entity.setDev(0);
        } else {
            entity.setDev(1);
        }
    }


    public List<Map<String, String>> showHardwareMonitorInHour() {
//        String previousTime = DateConverter.getNoSymbolTime(System.currentTimeMillis() - 3600000);
//        return ListConverter.mapValueIsString(monitorMapper.showHardwareMonitorInHour(previousTime));
        return daoList;
    }

}

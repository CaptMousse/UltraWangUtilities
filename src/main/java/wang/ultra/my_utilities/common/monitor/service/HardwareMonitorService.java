package wang.ultra.my_utilities.common.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;
import wang.ultra.my_utilities.common.monitor.mapper.MonitorMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.*;

@Service("hardwareMonitorService")
public class HardwareMonitorService {

    @Autowired
    MonitorMapper monitorMapper;

    public void hardwareMonitorRecord(MonitorEntity entity) {
        getBasicInfo(entity);
        entity.setType(1);

        List<MonitorEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        monitorMapper.addMonitorInfo(entityList);
        monitorMapper.addHardwareCpuUsage(entityList);
        monitorMapper.addHardwareCpuTemperature(entityList);
        monitorMapper.addHardwareMemoryUsage(entityList);
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
        return ListConverter.mapValueIsString(monitorMapper.showHardwareMonitorInHour());
    }
}

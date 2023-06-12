package wang.ultra.my_utilities.common.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import wang.ultra.my_utilities.common.monitor.entity.MonitorEntity;

import java.util.List;
import java.util.Map;

public interface MonitorMapper {

    void addMonitorInfo(List<MonitorEntity> monitorEntityList);

    void addHardwareCpuUsage(List<MonitorEntity> entityList);

    void addHardwareCpuTemperature(List<MonitorEntity> entityList);

    void addHardwareMemoryUsage(List<MonitorEntity> entityList);

    void recoderDdnsIpv6(List<MonitorEntity> entityList);

    List<Map<String, Object>> showHardwareMonitorInHour(@Param("previousTime") String previousTime);
}

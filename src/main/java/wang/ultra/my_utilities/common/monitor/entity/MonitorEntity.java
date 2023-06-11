package wang.ultra.my_utilities.common.monitor.entity;

import lombok.Data;

@Data
public class MonitorEntity {
    private String id;
    private String record_time;
    private Integer dev;
    private Integer type;
    private String cpuUsage;
    private String cpuTemperature;
    private String memoryUsage;
    private String ddnsIpv6;

}

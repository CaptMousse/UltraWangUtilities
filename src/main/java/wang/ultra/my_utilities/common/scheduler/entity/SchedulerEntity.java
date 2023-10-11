package wang.ultra.my_utilities.common.scheduler.entity;

import lombok.Data;

@Data
public class SchedulerEntity {
    private String id;
    private String jobName;
    private String triggerName;
    private String cronExpression;
    private String jobClass;
    private String status;
    private String isRunning;
}

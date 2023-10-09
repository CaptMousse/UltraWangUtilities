package wang.ultra.my_utilities.stock_exchange.entity;

import lombok.Data;

@Data
public class SchedulerEntity {
    private String id;
    private String jobName;
    private String triggerName;
    private String cronExpression;
    private String jobClass;
    private String status;
}

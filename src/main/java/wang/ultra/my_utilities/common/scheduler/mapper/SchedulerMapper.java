package wang.ultra.my_utilities.common.scheduler.mapper;

import wang.ultra.my_utilities.common.scheduler.entity.SchedulerEntity;

import java.util.List;
import java.util.Map;

public interface SchedulerMapper {

    void add(List<SchedulerEntity> entities);

    void updateRunningStatus(String jobName, String runningStatus);

    List<Map<String, Object>> getAllJob();

    List<Map<String, Object>> getRunningJob();

    Map<String, Object> getJob(String jobName);
}

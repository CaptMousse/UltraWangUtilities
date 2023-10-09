package wang.ultra.my_utilities.stock_exchange.mapper;

import wang.ultra.my_utilities.stock_exchange.entity.SchedulerEntity;

import java.util.List;
import java.util.Map;

public interface SchedulerMapper {

    void add(List<SchedulerEntity> entities);
    List<Map<String, Object>> getAll();

    Map<String, Object> getJob(String jobName);
}

package wang.ultra.my_utilities.stock_exchange.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.stock_exchange.entity.SchedulerEntity;
import wang.ultra.my_utilities.stock_exchange.mapper.SchedulerMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("stockSchedulerService")
public class StockSchedulerService {

    @Autowired
    SchedulerMapper schedulerMapper;

    public void add() {
        List<Map<String, String>> quartzList = new ArrayList<>();

        List<SchedulerEntity> entities = new ArrayList<>();
        SchedulerEntity entity1 = new SchedulerEntity();
        entity1.setId(StringUtils.getMyUUID());
        entity1.setJobName("stock10DaysMACDJob");
        entity1.setTriggerName("stock10DaysMACDTrigger");
        entity1.setCronExpression("0 30 18 * * ? *");
        entity1.setJobClass("wang.ultra.my_utilities.stock_exchange.tasks.Stock10DaysMACDTask");
        entity1.setStatus("1");
        SchedulerEntity entity2 = new SchedulerEntity();
        entity2.setId(StringUtils.getMyUUID());
        entity2.setJobName("stockRecentDayMAJob");
        entity2.setTriggerName("stockRecentDayMATrigger");
        entity2.setCronExpression("0 30 18 * * ? *");
        entity2.setJobClass("wang.ultra.my_utilities.stock_exchange.tasks.StockRecentDayMATask");
        entity2.setStatus("1");
        SchedulerEntity entity3 = new SchedulerEntity();
        entity3.setId(StringUtils.getMyUUID());
        entity3.setJobName("testJob");
        entity3.setTriggerName("testTrigger");
        entity3.setJobClass("wang.ultra.my_utilities.stock_exchange.scheduler.quartz.QuartzTest");
        entity2.setCronExpression("* * * * * ? *");
        entity3.setStatus("1");
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        schedulerMapper.add(entities);
    }

    /**
     * 获取所有任务
     * @return
     */
    public List<Map<String, String>> getAllJob() {
        return ListConverter.mapValueToString(schedulerMapper.getAll());
    }

    /**
     * 获取指定任务
     * @param jobName
     * @return
     */
    public Map<String, String> getJob(String jobName) {
        return ListConverter.mapValueToString(schedulerMapper.getJob(jobName));
    }

    /**
     * 启动任务
     * @param jobName
     * @return
     */
    public Integer startTask(String jobName) {
        Map<String, String> jobMap = getJob(jobName);
        if (jobMap.isEmpty()) {
            return -2;
        }
        try {
            launchTask(getJob(jobName));
            return 1;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 停止任务
     * @param jobName
     * @return
     */
    public Integer stopTask(String jobName) {
        Map<String, String> jobMap = getJob(jobName);
        String triggerName = jobMap.get("trigger_name");
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            TriggerKey triggerKey = new TriggerKey(triggerName);
            scheduler.unscheduleJob(triggerKey);
            return 1;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }

    }

    private void launchTask(Map<String, String> quartzMap) throws SchedulerException {
        Class<Job> clazz;
        try {
            clazz = (Class<Job>) Class.forName(quartzMap.get("job_class"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(quartzMap.get("job_name"))
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzMap.get("trigger_name"))
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzMap.get("cron_expression")))
                .build();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}

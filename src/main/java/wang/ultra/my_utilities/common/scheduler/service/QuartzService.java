package wang.ultra.my_utilities.common.scheduler.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.common.scheduler.entity.SchedulerEntity;
import wang.ultra.my_utilities.common.scheduler.mapper.SchedulerMapper;

import java.util.*;

@Service("quartzService")
public class QuartzService {

    @Autowired
    SchedulerMapper schedulerMapper;

    public List<Map<String, String>> getRunningList() {
        Scheduler scheduler;
        GroupMatcher<JobKey> groupMatcher = GroupMatcher.anyGroup();
        Set<JobKey> jobKeySet;
        List<Map<String, String>> jobList = new ArrayList<>();
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            jobKeySet = scheduler.getJobKeys(groupMatcher);
            for (JobKey jobKey : jobKeySet) {
                List<? extends Trigger> triggerList = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggerList) {
                    Map<String, String> jobMap = new LinkedHashMap<>();
                    jobMap.put("JobName", jobKey.getName());
                    jobMap.put("JobGroup", jobKey.getGroup());
                    jobMap.put("TriggerKey", String.valueOf(trigger.getKey()));
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    jobMap.put("TriggerStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        jobMap.put("CronExpression", ((CronTrigger) trigger).getCronExpression());
                    }
                    jobList.add(jobMap);
                }
            }
            return jobList;
        } catch (SchedulerException e) {
            return null;
        }
    }

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
     * 校验任务是否已经被运行
     * @param jobName
     * @return
     */
    public boolean getTaskStatus(String jobName) {
        if (null == jobName || jobName.isEmpty()) {
            return false;
        }
        List<Map<String, String>> jobList = getRunningList();
        for (Map<String, String> job : jobList) {
            String name = job.get("JobName");
            if (name.equals(jobName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有任务
     * @return
     */
    public List<Map<String, String>> getAllJob() {
        return ListConverter.mapValueToString(schedulerMapper.getAllJob());
    }

    /**
     * 获取正在运行的任务
     * @return
     */
    public List<Map<String, String>> getRunningJob() {
        return ListConverter.mapValueToString(schedulerMapper.getRunningJob());
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
            if (getTaskStatus(jobName)) {
                return 0;
            }
            launchTask(jobMap);

            updateRunningStatus(jobName, "1");

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
        if (jobMap.isEmpty()) {
            return -2;
        }
        String triggerName = jobMap.get("trigger_name");
        try {
            if (getTaskStatus(jobName)) {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                TriggerKey triggerKey = new TriggerKey(triggerName);
                scheduler.unscheduleJob(triggerKey);

                updateRunningStatus(jobName, "0");

                return 1;
            }
            return 0;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 运行定时任务
     * @param quartzMap
     * @throws SchedulerException
     */
    public void launchTask(Map<String, String> quartzMap) throws SchedulerException {
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

    private void updateRunningStatus(String jobName, String runningStatus) {
        schedulerMapper.updateRunningStatus(jobName, runningStatus);
    }
}

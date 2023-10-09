package wang.ultra.my_utilities.common.scheduler.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class QuartzCore {
    @Autowired
    QuartzService quartzService;

    public void quartzInit() throws SchedulerException {

        List<Map<String, String>> quartzList = quartzService.getAllJob();


        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        for (Map<String, String> quartzMap : quartzList) {

            Class<Job> clazz;
            try {
                clazz = (Class<Job>) Class.forName(quartzMap.get("JobClass"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            JobDetail jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity(quartzMap.get("JobName"))
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzMap.get("TriggerName"))
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzMap.get("CronExpression")))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

}

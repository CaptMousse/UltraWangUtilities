package wang.ultra.my_utilities.common.scheduler.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface BaseJobService extends Job {


     void runJob();
}

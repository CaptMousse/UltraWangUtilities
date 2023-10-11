package wang.ultra.my_utilities.stock_exchange.SchedulerTasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzTest implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(QuartzTest.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("RUUUN!!! ");
    }
}

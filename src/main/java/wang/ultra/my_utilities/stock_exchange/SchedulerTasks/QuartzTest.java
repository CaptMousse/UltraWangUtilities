package wang.ultra.my_utilities.stock_exchange.SchedulerTasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTest implements Job {
    private static final Log LOG = LogFactory.getLog(QuartzTest.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("RUUUN!!! ");
    }
}

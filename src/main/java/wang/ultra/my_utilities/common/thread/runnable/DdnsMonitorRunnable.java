package wang.ultra.my_utilities.common.thread.runnable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.aliyun_ddns_update.service.DdnsMonitorService;

public class DdnsMonitorRunnable implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(DdnsMonitorRunnable.class);

    @Override
    public void run() {
        LOG.info("DdnsMonitorService Starting...");
        DdnsMonitorService.loop();
    }
}

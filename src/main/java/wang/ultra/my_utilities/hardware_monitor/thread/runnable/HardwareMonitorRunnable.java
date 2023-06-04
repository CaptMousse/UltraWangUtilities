package wang.ultra.my_utilities.hardware_monitor.thread.runnable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.hardware_monitor.service.MonitorService;

public class HardwareMonitorRunnable implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(HardwareMonitorRunnable.class);
    @Override
    public void run() {
        LOG.info("HardwareMonitorService Starting...");
        MonitorService  monitorService = new MonitorService();
        monitorService.loop();
    }
}

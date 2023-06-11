package wang.ultra.my_utilities.hardware_monitor.runnable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.hardware_monitor.service.MonitorService;

public class HardwareMonitorRunnable implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(HardwareMonitorRunnable.class);
    @Override
    public void run() {
        if (ConstantFromFile.getHardwareMonitorStatus().equals("1")) {
            MonitorService monitorService = new MonitorService();
            monitorService.monitor();
        }
    }
}

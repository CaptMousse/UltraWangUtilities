package wang.ultra.my_utilities.hardware_monitor.runnable;


import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.hardware_monitor.service.MonitorService;

public class HardwareMonitorRunnable implements Runnable{

    @Override
    public void run() {
        if (ConstantFromFile.getHardwareMonitorStatus().equals("1")) {
            MonitorService monitorService = new MonitorService();
            monitorService.monitor();
        }
    }
}

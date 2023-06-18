package wang.ultra.my_utilities.common.schedules;

import wang.ultra.my_utilities.aliyun_ddns_update.runnable.DdnsMonitorRunnable;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.hardware_monitor.runnable.HardwareMonitorRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonScheduler {


    public void hardwareMonitorSchedule() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        Long ddnsMonitorPeriod = ConstantFromFile.getDdnsIntervalTime();
        ses.scheduleAtFixedRate(new DdnsMonitorRunnable(), 0, ddnsMonitorPeriod, TimeUnit.HOURS);

        Long hardrwareMonitorPeriod = ConstantFromFile.getMonitorRate();
        ses.scheduleAtFixedRate(new HardwareMonitorRunnable(), 0, hardrwareMonitorPeriod, TimeUnit.MINUTES);
    }
}

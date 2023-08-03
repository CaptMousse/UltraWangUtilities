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

        String hardwareMonitorPeriod = ConstantFromFile.getMonitorRate();
        long monitorRate = Long.parseLong(hardwareMonitorPeriod.substring(0, hardwareMonitorPeriod.length() - 1));
        String hardwareMonitorUnit = hardwareMonitorPeriod.substring(hardwareMonitorPeriod.length() - 1);
        if ("h".equals(hardwareMonitorUnit)) {
            System.out.println("hhh");
            ses.scheduleAtFixedRate(new HardwareMonitorRunnable(), 0, monitorRate, TimeUnit.HOURS);
        }
        if ("m".equals(hardwareMonitorUnit)) {
            System.out.println("mmm");
            ses.scheduleAtFixedRate(new HardwareMonitorRunnable(), 0, monitorRate, TimeUnit.MINUTES);
        }
        if ("s".equals(hardwareMonitorUnit)) {
            System.out.println("sss");
            ses.scheduleAtFixedRate(new HardwareMonitorRunnable(), 0, monitorRate, TimeUnit.SECONDS);
        }
    }
}

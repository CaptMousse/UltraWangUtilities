package wang.ultra.my_utilities.hardware_monitor.thread;


import wang.ultra.my_utilities.hardware_monitor.thread.runnable.HardwareMonitorRunnable;

public class HardwareMonitorThread {

    public void hardwareMonitorThreadStart() {
        HardwareMonitorRunnable hardwareMonitorRunnable = new HardwareMonitorRunnable();
        Thread hardwareMonitorThread = new Thread(hardwareMonitorRunnable, "HARDWARE_MONITOR");
        hardwareMonitorThread.start();
    }
}

package wang.ultra.my_utilities.common.thread;


import wang.ultra.my_utilities.common.thread.runnable.HardwareMonitorRunnable;

public class HardwareMonitorThread {

    public void hardwareMonitorThreadStart() {
        HardwareMonitorRunnable hardwareMonitorRunnable = new HardwareMonitorRunnable();
        Thread hardwareMonitorThread = new Thread(hardwareMonitorRunnable, "HARDWARE_MONITOR");
        hardwareMonitorThread.start();
    }
}

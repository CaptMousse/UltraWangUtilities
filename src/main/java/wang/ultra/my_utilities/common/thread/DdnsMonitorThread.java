package wang.ultra.my_utilities.common.thread;

import wang.ultra.my_utilities.common.thread.runnable.DdnsMonitorRunnable;

public class DdnsMonitorThread {

    public void ddnsMonitorThreadStart() {
        DdnsMonitorRunnable ddnsMonitorRunnable = new DdnsMonitorRunnable();
        Thread ddnsMonitorThread = new Thread(ddnsMonitorRunnable, "DDNS_MONITOR");
        ddnsMonitorThread.start();
    }
}

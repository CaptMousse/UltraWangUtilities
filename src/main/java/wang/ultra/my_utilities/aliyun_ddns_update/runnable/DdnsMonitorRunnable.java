package wang.ultra.my_utilities.aliyun_ddns_update.runnable;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wang.ultra.my_utilities.aliyun_ddns_update.service.DdnsMonitorService;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

public class DdnsMonitorRunnable implements Runnable {
    private static final Log LOG = LogFactory.getLog(DdnsMonitorRunnable.class);

    @Override
    public void run() {

        if (ConstantFromFile.getDdnsMonitorStatus().equals("1")) {
            DdnsMonitorService ddnsMonitorService = new DdnsMonitorService();
            ddnsMonitorService.monitor();
        }

    }
}

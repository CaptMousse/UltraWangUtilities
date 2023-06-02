package wang.ultra.my_utilities.aliyun_ddns_update.service;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.aliyun_ddns_update.utils.DDNSUtils;
import wang.ultra.my_utilities.aliyun_ddns_update.utils.GetMyIPv6;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;

import static wang.ultra.my_utilities.common.utils.FileIOUtils.createFile;

public class DdnsMonitorService {
    private static final Logger LOG = LoggerFactory.getLogger(DdnsMonitorService.class);

    private static Integer statusResult = -1;

    private static String nextUpdateTime = null;

    public static Integer getStatusResult() {
        return statusResult;
    }

    public static void setStatusResult(Integer statusResult) {
        DdnsMonitorService.statusResult = statusResult;
    }

    public static String getNextUpdateTime() {
        return nextUpdateTime;
    }

    public static void setNextUpdateTime(String time) {
        DdnsMonitorService.nextUpdateTime = time;
    }

    /**
     * 获取更新状态
     */
    public static void getStatus() {

        DDNSUtils.setDomainRecords();
        System.out.println("\n==---->> Describing Time = " + DateConverter.getTime());

        if (DDNSUtils.getDomainRecords().size() != 0) {
            DescribeDomainRecordsResponse.Record record = DDNSUtils.getDomainRecords().get(0);
            String recordsHostIP = record.getValue();
            String currentHostIP = GetMyIPv6.getIPv6();

            if (currentHostIP.equals("-1")) {
                statusResult = Integer.parseInt(currentHostIP);
                System.out.println("==---->> IPv6 address may not exist!");
                return;
            }

            System.out.println("==---->> recordsHostIP = " + recordsHostIP);
            System.out.println("==---->> currentHostIP = " + currentHostIP);
            if (!currentHostIP.equals(recordsHostIP)) {
                statusResult = 1; // 需要更新
                return;
            }
            statusResult = 0;     // 无需更新
            return;
        }
        statusResult = -1;        // 状态出错
    }


    /**
     * 根据状态更新
     *
     * @return -1 = 更新失败, 0 = 无需更新, 1 = 更新成功;
     */
    public static Integer update() {

        if (statusResult == 1) {
            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
            updateDomainRecordRequest.setRecordId(DDNSUtils.getDomainRecords().get(0).getRecordId());
            updateDomainRecordRequest.setValue(GetMyIPv6.getIPv6());
            updateDomainRecordRequest.setRR(ConstantFromFile.getHostname());
            updateDomainRecordRequest.setType(ConstantFromFile.getRecordType());

            DDNSUtils ddnsUtils = new DDNSUtils();
            ddnsUtils.updateDomainRecord(updateDomainRecordRequest, DDNSUtils.getClient());
            System.out.println("==---->> Domain Record is up to date.");

            // 记录日志
//            String subPath = "Logs";
//            String fileName = "IPv6Logs.txt";
            String text = DateConverter.getTime() + " = " + updateDomainRecordRequest.getValue();
            createFile("Logs", "IPv6Logs.txt", 1, text);

            statusResult = 0;

            return 1;
        }
        if (statusResult == 0) {
            return 0;
        }
        return -1;
    }

    // 监听
    public static void loop()  {
        // 获取监听状态 0 - 停止, 1 - 运行
        String ddnsMonitorStatus = ConstantFromFile.getDdnsMonitorStatus();

        LOG.info("DdnsMonitorService Started!");
        while (ddnsMonitorStatus.equals("1")) {
            getStatus();
            if (statusResult == 1) {
                // 如果需要更新
                update();
            }
            // 记录下次更新时间
            nextUpdateTime = DateConverter.getTime(System.currentTimeMillis() + ConstantFromFile.getDdnsIntervalTime());

            try {
                Thread.sleep(ConstantFromFile.getDdnsIntervalTime());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ddnsMonitorStatus = ConstantFromFile.getDdnsMonitorStatus();
        }
    }
}

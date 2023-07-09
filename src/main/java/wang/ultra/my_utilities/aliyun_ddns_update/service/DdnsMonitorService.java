package wang.ultra.my_utilities.aliyun_ddns_update.service;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.aliyun_ddns_update.utils.DDNSUtils;
import wang.ultra.my_utilities.aliyun_ddns_update.utils.GetHostIPv4;
import wang.ultra.my_utilities.aliyun_ddns_update.utils.GetMyIPv6;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.FileIOUtils;

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

        String recordType = ConstantFromFile.getRecordType();
        String ipType = "域名解析";
        String currentHostIP = "-1";
        if (recordType.equals("AAAA")) {
            ipType = "IPv6";
            currentHostIP = GetMyIPv6.getIPv6();
        } else if (recordType.equals("A")) {
            ipType = "IPv4";
            GetHostIPv4 getHostIPv4 = new GetHostIPv4();
            currentHostIP = getHostIPv4.getHostIPv4();
        }

        DDNSUtils.setDomainRecords();
        System.out.println("\n==---->> " + ipType + "地址比对时间 = " + DateConverter.getTime());

        if (DDNSUtils.getDomainRecords().size() != 0) {
            DescribeDomainRecordsResponse.Record record = DDNSUtils.getDomainRecords().get(0);
            String recordsHostIP = record.getValue();
            if (!currentHostIP.equals(recordsHostIP)) {
                System.out.println("==---->> " + ipType + "地址当前值 = " + currentHostIP);
                System.out.println("==---->> " + ipType + "地址记录值 = " + recordsHostIP);
                statusResult = 1; // 需要更新
                return;
            }

            if (currentHostIP.equals("-1")) {
                statusResult = Integer.parseInt(currentHostIP);
                System.out.println("==---->> " + ipType + "地址可能不存在");
                return;
            }

            System.out.println("==---->> " + ipType + "地址未变化 = " + currentHostIP);
            statusResult = 0;     // 无需更新
            return;
        } else {
            System.out.println("==---->> " + ipType + "地址更新失败");
        }
        statusResult = -1;        // 状态出错
    }


    /**
     * 根据状态更新
     *
     * @return -1 = 更新失败, 0 = 无需更新, 1 = 更新成功;
     */
    public static Integer update() {

        String recordType = ConstantFromFile.getRecordType();
        String currentHostIP = null;
        if (recordType.equals("AAAA")) {
            currentHostIP = GetMyIPv6.getIPv6();
        } else if (recordType.equals("A")) {
            GetHostIPv4 getHostIPv4 = new GetHostIPv4();
            currentHostIP = getHostIPv4.getHostIPv4();
        }

        if (statusResult == 1) {
            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
            updateDomainRecordRequest.setRecordId(DDNSUtils.getDomainRecords().get(0).getRecordId());
            updateDomainRecordRequest.setValue(currentHostIP);
            updateDomainRecordRequest.setRR(ConstantFromFile.getHostname());
            updateDomainRecordRequest.setType(ConstantFromFile.getRecordType());

            DDNSUtils ddnsUtils = new DDNSUtils();
            ddnsUtils.updateDomainRecord(updateDomainRecordRequest, DDNSUtils.getClient());
            System.out.println("==---->> IPv6地址更新成功!");

            // 记录日志
//            String subPath = "Logs";
//            String fileName = "IPv6Logs.txt";
            String text = DateConverter.getTime() + " = " + updateDomainRecordRequest.getValue();
            FileIOUtils.createFile("Logs", "IPv6Logs.txt", 1, text);

            statusResult = 0;
            return 1;
        }
        if (statusResult == 0) {
            return 0;
        }
        return -1;
    }

    // 监听
    public void monitor()  {

            getStatus();
            if (statusResult == 1) {
                // 如果需要更新
                update();
            }
            // 记录下次更新时间
            nextUpdateTime = DateConverter.getTime(System.currentTimeMillis() + ConstantFromFile.getDdnsIntervalTime() * 3600 * 1000);
    }
}

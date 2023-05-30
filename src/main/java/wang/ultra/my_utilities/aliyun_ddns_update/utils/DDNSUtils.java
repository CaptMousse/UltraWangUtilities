package wang.ultra.my_utilities.aliyun_ddns_update.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

import java.util.List;

public class DDNSUtils {

    private static Integer result = -1;

    private static String nextUpdateTime = null;

    private static IAcsClient client = null;

    private static List<DescribeDomainRecordsResponse.Record> domainRecords = null;

    public static Integer getResult() {
        return result;
    }

    public static void setResult(Integer result) {
        DDNSUtils.result = result;
    }

    public static String getNextUpdateTime() {
        return nextUpdateTime;
    }

    public static void setNextUpdateTime(String time) {
        DDNSUtils.nextUpdateTime = time;
    }

    public static IAcsClient getClient() {
        return client;
    }

    public static void setClient(IAcsClient client) {
        DDNSUtils.client = client;
    }

    public static List<DescribeDomainRecordsResponse.Record> getDomainRecords() {
        return domainRecords;
    }

    public static void setDomainRecords(List<DescribeDomainRecordsResponse.Record> domainRecords) {
        DDNSUtils.domainRecords = domainRecords;
    }



    // 获得域名解析记录
    private DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request, IAcsClient client) {
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



    // 更新域名解析记录
    public UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request, IAcsClient client) {
        try {
            //
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



    // 设置绑定域名
    public static void setDomainRecords() {

        // 获取地域ID, AccessKeyID, AccessKeySecret
        DefaultProfile profile = DefaultProfile.getProfile(ConstantFromFile.getRegionID(), ConstantFromFile.getAccessKeyID(), ConstantFromFile.getAccessKeySecret());
        client = new DefaultAcsClient(profile);

        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        describeDomainRecordsRequest.setDomainName(ConstantFromFile.getDomain());   // 域名(ultra.wang)
        describeDomainRecordsRequest.setRRKeyWord(ConstantFromFile.getHostname());  // 主机记录(二级域名minecraft)
        describeDomainRecordsRequest.setType(ConstantFromFile.getRecordType());     // 记录类型(IPv6 AAAA)

        DDNSUtils ddnsUtils = new DDNSUtils();
        DescribeDomainRecordsResponse describeDomainRecordsResponse = ddnsUtils.describeDomainRecords(describeDomainRecordsRequest, client);

        domainRecords = describeDomainRecordsResponse.getDomainRecords();
    }

}
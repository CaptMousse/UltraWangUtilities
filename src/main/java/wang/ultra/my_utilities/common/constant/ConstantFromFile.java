package wang.ultra.my_utilities.common.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.ultra.my_utilities.common.utils.FileIOUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConstantFromFile {
    private static final Logger LOG = LoggerFactory.getLogger(ConstantFromFile.class);

    // DDNS监听时间
    private static Long DdnsIntervalTime;
    // DDNS监控开关
    private static String DdnsMonitorStatus;
    // 地域ID
    private static String RegionID;
    // AccessKey ID
    private static String AccessKeyID;
    // AccessKey Secret
    private static String AccessKeySecret;

    // 域名
    private static String Domain;

    // 二级域名
    private static String Hostname;

    // 记录类型(IPv6)
    private static String RecordType;

    // MC端口
    private static String MinecraftPort;

    // 网卡名
    private static String EthernetName;

    // 日志文件目录
    private static String LogFilePath;

    // 日志文件名
    private static String LogFileName;

    // 邮件发送主机
    private static String MailHost;

    // 邮件发送协议
    private static String MailTransportProtocol;

    // 邮件SMTP端口
    private static String MailSmtpPort;

    // 邮件发件人邮箱
    private static String MailFrom;

    // 邮件发件人授权码
    private static String MailFromAuthCode;

    // 邮件收件人
    private static String MailTo;

    // 监控开关
    private static String HardwareMonitorStatus;

    // 监控频率
    private static Long MonitorRate;

    private static Double MonitorCpuUsage;

    // 监控CPU温度
    private static Double MonitorCpuTemperature;

    // 监控内存变化
    private static Double MonitorMemoryChange;

    // 限流峰值
    private static Long LimitingPeak;

    // 限流QPS
    private static Long LimitingQPS;


    private static Map<String, String> constMap = new HashMap<>();

    /**
     * 初始化配置文件
     *
     * @return
     */
    public static Integer setConstFromMap() {
        LOG.info("配置文件正在加载...");
        String filePath = System.getProperty("user.dir") + File.separator + "Constants";
        String fileName = "Constants.txt";
        ConstantFromFile.constMap = FileIOUtils.readFile(filePath, fileName);

        System.out.println("constMap = " + ConstantFromFile.constMap);

        if (!ConstantFromFile.constMap.isEmpty()) {
            ConstantFromFile.DdnsIntervalTime = Long.valueOf(constMap.get("DdnsIntervalTime"));
            ConstantFromFile.DdnsMonitorStatus = constMap.get("DdnsMonitorStatus");
            ConstantFromFile.RegionID = constMap.get("RegionID");
            ConstantFromFile.AccessKeyID = constMap.get("AccessKeyID");
            ConstantFromFile.AccessKeySecret = constMap.get("AccessKeySecret");
            ConstantFromFile.Domain = constMap.get("Domain");
            ConstantFromFile.Hostname = constMap.get("Hostname");
            ConstantFromFile.RecordType = constMap.get("RecordType");

            ConstantFromFile.MinecraftPort = constMap.get("MinecraftPort");

            ConstantFromFile.EthernetName = constMap.get("EthernetName");

            ConstantFromFile.LogFilePath = constMap.get("LogFilePath");
            ConstantFromFile.LogFileName = constMap.get("LogFileName");

            ConstantFromFile.MailHost = constMap.get("MailHost");
            ConstantFromFile.MailTransportProtocol = constMap.get("MailTransportProtocol");
            ConstantFromFile.MailSmtpPort = constMap.get("MailSmtpPort");
            ConstantFromFile.MailFrom = constMap.get("MailFrom");
            ConstantFromFile.MailFromAuthCode = constMap.get("MailFromAuthCode");
            ConstantFromFile.MailTo = constMap.get("MailTo");

            ConstantFromFile.HardwareMonitorStatus = constMap.get("HardwareMonitorStatus");
            ConstantFromFile.MonitorRate = Long.valueOf(constMap.get("MonitorRate"));
            ConstantFromFile.MonitorCpuUsage = Double.valueOf(constMap.get("MonitorCpuUsage"));
            ConstantFromFile.MonitorCpuTemperature = Double.valueOf(constMap.get("MonitorCpuTemperature"));
            ConstantFromFile.MonitorMemoryChange = Double.valueOf(constMap.get("MonitorMemoryChange"));

            ConstantFromFile.LimitingPeak = Long.valueOf(constMap.get("LimitingPeak"));
            ConstantFromFile.LimitingQPS = Long.valueOf(constMap.get("LimitingQPS"));
            LOG.info("配置文件加载成功!");
            return 1;
        }
        LOG.error("配置文件加载失败!");
        return -1;
    }

    public static Long getDdnsIntervalTime() {
        return DdnsIntervalTime;
    }

    public static void setDdnsIntervalTime(Long ddnsIntervalTime) {
        DdnsIntervalTime = ddnsIntervalTime;
    }

    public static String getDdnsMonitorStatus() {
        return DdnsMonitorStatus;
    }

    public static void setDdnsMonitorStatus(String ddnsMonitorStatus) {
        DdnsMonitorStatus = ddnsMonitorStatus;
    }

    public static String getRegionID() {
        return RegionID;
    }

    public static void setRegionID(String regionID) {
        RegionID = regionID;
    }

    public static String getAccessKeyID() {
        return AccessKeyID;
    }

    public static void setAccessKeyID(String accessKeyID) {
        AccessKeyID = accessKeyID;
    }

    public static String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public static String getDomain() {
        return Domain;
    }

    public static void setDomain(String domain) {
        Domain = domain;
    }

    public static String getHostname() {
        return Hostname;
    }

    public static void setHostname(String hostname) {
        Hostname = hostname;
    }

    public static String getRecordType() {
        return RecordType;
    }

    public static void setRecordType(String recordType) {
        RecordType = recordType;
    }

    public static String getMinecraftPort() {
        return MinecraftPort;
    }

    public static void setMinecraftPort(String minecraftPort) {
        MinecraftPort = minecraftPort;
    }

    public static Map<String, String> getConstMap() {
        return constMap;
    }

    public static void setConstMap(Map<String, String> constMap) {
        ConstantFromFile.constMap = constMap;
    }

    public static String getEthernetName() {
        return EthernetName;
    }

    public static void setEthernetName(String ethernetName) {
        EthernetName = ethernetName;
    }

    public static String getLogFilePath() {
        return LogFilePath;
    }

    public static void setLogFilePath(String logFilePath) {
        LogFilePath = logFilePath;
    }

    public static String getLogFileName() {
        return LogFileName;
    }

    public static void setLogFileName(String logFileName) {
        LogFileName = logFileName;
    }

    public static String getMailHost() {
        return MailHost;
    }

    public static void setMailHost(String mailHost) {
        MailHost = mailHost;
    }

    public static String getMailTransportProtocol() {
        return MailTransportProtocol;
    }

    public static void setMailTransportProtocol(String mailTransportProtocol) {
        MailTransportProtocol = mailTransportProtocol;
    }

    public static String getMailSmtpPort() {
        return MailSmtpPort;
    }

    public static void setMailSmtpPort(String mailSmtpPort) {
        MailSmtpPort = mailSmtpPort;
    }

    public static String getMailFrom() {
        return MailFrom;
    }

    public static void setMailFrom(String mailFrom) {
        MailFrom = mailFrom;
    }

    public static String getMailFromAuthCode() {
        return MailFromAuthCode;
    }

    public static void setMailFromAuthCode(String mailFromAuthCode) {
        MailFromAuthCode = mailFromAuthCode;
    }

    public static String getMailTo() {
        return MailTo;
    }

    public static void setMailTo(String mailTo) {
        MailTo = mailTo;
    }

    public static String getHardwareMonitorStatus() {
        return HardwareMonitorStatus;
    }

    public static void setHardwareMonitorStatus(String hardwareMonitorStatus) {
        HardwareMonitorStatus = hardwareMonitorStatus;
    }

    public static Long getMonitorRate() {
        return MonitorRate;
    }

    public static void setMonitorRate(Long monitorRate) {
        MonitorRate = monitorRate;
    }

    public static Double getMonitorCpuUsage() {
        return MonitorCpuUsage;
    }

    public static void setMonitorCpuUsage(Double monitorCpuUsage) {
        MonitorCpuUsage = monitorCpuUsage;
    }

    public static Double getMonitorCpuTemperature() {
        return MonitorCpuTemperature;
    }

    public static void setMonitorCpuTemperature(Double monitorCpuTemperature) {
        MonitorCpuTemperature = monitorCpuTemperature;
    }

    public static Double getMonitorMemoryChange() {
        return MonitorMemoryChange;
    }

    public static void setMonitorMemoryChange(Double monitorMemoryChange) {
        MonitorMemoryChange = monitorMemoryChange;
    }

    public static Long getLimitingPeak() {
        return LimitingPeak;
    }

    public static void setLimitingPeak(Long limitingPeak) {
        LimitingPeak = limitingPeak;
    }

    public static Long getLimitingQPS() {
        return LimitingQPS;
    }

    public static void setLimitingQPS(Long limitingQPS) {
        LimitingQPS = limitingQPS;
    }


}

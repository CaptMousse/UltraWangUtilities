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

    // Minecraft资料文件夹
    private static String MinecraftDataFolder;

    // Minecraft黑名单文件名
    private static String MinecraftBlackListFileName;

    // Minecraft白名单文件名
    private static String MinecraftWhiteListFileName;

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
    private static String MonitorRate;

    private static Double MonitorCpuUsage;

    // 监控CPU温度
    private static Double MonitorCpuTemperature;

    // 监控内存变化
    private static Double MonitorMemoryChange;

    // 限流峰值
    private static Long LimitingPeak;

    // 限流QPS
    private static Long LimitingQPS;

    // 文件上传下载文件夹
    private static String FileFolder;

    // 用户登陆过期时间(秒)
    private static Integer SessionInactiveInterval;

    // 股票麦蕊API的Licence
    private static String MairuiApiLicence;

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
        ConstantFromFile.constMap = FileIOUtils.readConfigFileToMap(filePath, fileName);

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
            ConstantFromFile.MinecraftDataFolder = constMap.get("MinecraftDataFolder");
            ConstantFromFile.MinecraftBlackListFileName = constMap.get("MinecraftBlackListFileName");
            ConstantFromFile.MinecraftWhiteListFileName = constMap.get("MinecraftWhiteListFileName");

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
            ConstantFromFile.MonitorRate = constMap.get("MonitorRate");
            ConstantFromFile.MonitorCpuUsage = Double.valueOf(constMap.get("MonitorCpuUsage"));
            ConstantFromFile.MonitorCpuTemperature = Double.valueOf(constMap.get("MonitorCpuTemperature"));
            ConstantFromFile.MonitorMemoryChange = Double.valueOf(constMap.get("MonitorMemoryChange"));

            ConstantFromFile.LimitingPeak = Long.valueOf(constMap.get("LimitingPeak"));
            ConstantFromFile.LimitingQPS = Long.valueOf(constMap.get("LimitingQPS"));

            ConstantFromFile.FileFolder = constMap.get("FileFolder");

            ConstantFromFile.SessionInactiveInterval = Integer.parseInt(constMap.get("SessionInactiveInterval"));

            ConstantFromFile.MairuiApiLicence = constMap.get("MairuiApiLicence");

            LOG.info("配置文件加载成功!");
            return 1;
        }
        LOG.error("配置文件加载失败!");
        return -1;
    }

    public static Long getDdnsIntervalTime() {
        return DdnsIntervalTime;
    }


    public static String getDdnsMonitorStatus() {
        return DdnsMonitorStatus;
    }


    public static String getRegionID() {
        return RegionID;
    }


    public static String getAccessKeyID() {
        return AccessKeyID;
    }


    public static String getAccessKeySecret() {
        return AccessKeySecret;
    }


    public static String getDomain() {
        return Domain;
    }


    public static String getHostname() {
        return Hostname;
    }


    public static String getRecordType() {
        return RecordType;
    }


    public static String getMinecraftPort() {
        return MinecraftPort;
    }

    public static String getMinecraftDataFolder() {
        return MinecraftDataFolder;
    }

    public static String getMinecraftBlackListFileName() {
        return MinecraftBlackListFileName;
    }

    public static String getMinecraftWhiteListFileName() {
        return MinecraftWhiteListFileName;
    }


    public static Map<String, String> getConstMap() {
        return constMap;
    }


    public static String getEthernetName() {
        return EthernetName;
    }


    public static String getLogFilePath() {
        return LogFilePath;
    }


    public static String getLogFileName() {
        return LogFileName;
    }


    public static String getMailHost() {
        return MailHost;
    }


    public static String getMailTransportProtocol() {
        return MailTransportProtocol;
    }


    public static String getMailSmtpPort() {
        return MailSmtpPort;
    }


    public static String getMailFrom() {
        return MailFrom;
    }


    public static String getMailFromAuthCode() {
        return MailFromAuthCode;
    }


    public static String getMailTo() {
        return MailTo;
    }


    public static String getHardwareMonitorStatus() {
        return HardwareMonitorStatus;
    }


    public static String getMonitorRate() {
        return MonitorRate;
    }


    public static Double getMonitorCpuUsage() {
        return MonitorCpuUsage;
    }


    public static Double getMonitorCpuTemperature() {
        return MonitorCpuTemperature;
    }


    public static Double getMonitorMemoryChange() {
        return MonitorMemoryChange;
    }


    public static Long getLimitingPeak() {
        return LimitingPeak;
    }


    public static Long getLimitingQPS() {
        return LimitingQPS;
    }

    public static String getFileFolder() {
        return FileFolder;
    }

    public static Integer getSessionInactiveInterval() {
        return SessionInactiveInterval;
    }

    public static String getMairuiApiLicence() {
        return MairuiApiLicence;
    }
}

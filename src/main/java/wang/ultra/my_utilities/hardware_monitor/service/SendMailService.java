package wang.ultra.my_utilities.hardware_monitor.service;


import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.SendMailUtils;

import java.util.Map;

public class SendMailService {

    public void sendCpuUsageMail(String cpuUsage) {
        String mailTo = ConstantFromFile.getMailTo();
        String mailSubject = "【UltraWang监控报警】在" + DateConverter.getSimpleTime() + "CPU使用率ß报警";
        String mailContent = "<h1 style=\"text-align: center;\">你家服务器的CPU都快累死了也不管嘛?</h1>" +
                "<h3 style=\"text-align: center;\">当前CPU使用率: " + cpuUsage + "</h3>";
        sendMailCore(mailTo, mailSubject, mailContent);
    }
    public void sendCpuTemperatureMail(String cpuTemperature) {
        String mailTo = ConstantFromFile.getMailTo();
        String mailSubject = "【UltraWang监控报警】在" + DateConverter.getSimpleTime() + "CPU温度报警";
        String mailContent = "<h1 style=\"text-align: center;\">康康你家服务器的CPU吧!</h1>" +
                "<h1 style=\"text-align: center;\">都快烧了也不管嘛?</h1>" +
                "<h3 style=\"text-align: center;\">当前温度: " + cpuTemperature + "</h3>";
        sendMailCore(mailTo, mailSubject, mailContent);
    }

    public void sendMemoryMail(Map<String, String> memoryMap) {
        String memoryChange = memoryMap.get("memoryChange");
        String memoryAvailableNow = memoryMap.get("memoryAvailableNow");
        String mailTo = ConstantFromFile.getMailTo();
        String mailSubject = "【UltraWang监控报警】在" + DateConverter.getSimpleTime() + "内存监控报警";
        String mailContent = "<h1 style=\"text-align: center;\">内存变化过快!</h1>" +
                "<h3 style=\"text-align: center;\">当前内存变动: " + memoryChange + "</h3>" +
                "<h3 style=\"text-align: center;\">当前内存使用率: " + memoryAvailableNow + "</h3>";

        sendMailCore(mailTo, mailSubject, mailContent);
    }

    private String sendMailCore(String mailTo, String mailSubject, String mailContent) {
        SendMailUtils sendMailUtils = new SendMailUtils();
        return sendMailUtils.sendMail(mailTo, mailSubject, mailContent);
    }



    public String sendTestMail(SendMailService sendMailService) {

        HardwareUsageService hardwareUsageService = new HardwareUsageService();
        String cpuTemperature = String.format("%.2f", hardwareUsageService.getCpuTemperature());
        String memoryUsage = String.format("%.2f", hardwareUsageService.getMemoryUsage());

        String mailTo = "sefvdx@me.com";
        String mailSubject = "【UltraWang监控提醒】在" + DateConverter.getSimpleTime() + "发送测试邮件";
        String mailContent = "<h1 style=\"text-align: center;\">歪比巴卜</h1>" +
                "<h3 style=\"text-align: center;\">当前CPU温度: " + cpuTemperature + "°C</h3>" +
                "<h3 style=\"text-align: center;\">当前内存使用率: " + memoryUsage + "%</h3>";
        return sendMailService.sendMailCore(mailTo, mailSubject, mailContent);
    }

//    public static void main(String[] args) {
//        SendMailService sendMailService = new SendMailService();
//        sendMailService.sendTestMail(sendMailService);
//    }
}

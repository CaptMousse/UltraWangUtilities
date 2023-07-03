package wang.ultra.my_utilities.hardware_monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.monitor.service.HardwareMonitorService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.hardware_monitor.service.HardwareUsageService;
import wang.ultra.my_utilities.hardware_monitor.service.SendMailService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/hardwareMonitor")
public class HardUsageController {

    @Autowired
    HardwareMonitorService hardwareMonitorService;

    /**
     * 获取硬件使用率
     * @return
     */
    @GetMapping("/hardwareUsage")
    public List<String> hardwareUsage() {
        HardwareUsageService hardwareUsageService = new HardwareUsageService();
        return hardwareUsageService.getAllUsage();
    }

    @GetMapping("/hardwareUsageInPastHour")
    public List<Map<String, String>> hardwareUsageInPastHour() {
        return hardwareMonitorService.showHardwareMonitorInHour();
    }

    @GetMapping("/sendTestMail")
    public String sendTestMail() {

        SendMailService sendMailService = new SendMailService();
        sendMailService.sendTestMail();

        return "<h1 style='text-align: center'></h1><br><h1 style='text-align: center'>山外青山楼外楼</h1><h1 style='text-align: center'>唱跳Rap打篮球</h1>";
    }
}

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
    public AjaxUtils hardwareUsage() {
        HardwareUsageService hardwareUsageService = new HardwareUsageService();
        return AjaxUtils.success(hardwareUsageService.getAllUsage());
    }

    @GetMapping("/hardwareUsageInPastHour")
    public AjaxUtils hardwareUsageInPastHour() {
        return AjaxUtils.success(hardwareMonitorService.getHourList());
    }

    @GetMapping("/sendTestMail")
    public AjaxUtils sendTestMail() {

        SendMailService sendMailService = new SendMailService();
        sendMailService.sendTestMail();

        return AjaxUtils.success(new StringBuilder("<h1 style='text-align: center'></h1><br><h1 style='text-align: center'>山外青山楼外楼</h1><h1 style='text-align: center'>唱跳Rap打篮球</h1>"));
    }
}

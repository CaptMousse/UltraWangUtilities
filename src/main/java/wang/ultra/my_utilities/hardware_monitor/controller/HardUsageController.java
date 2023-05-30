package wang.ultra.my_utilities.hardware_monitor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.hardware_monitor.service.HardwareUsageService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/hardwareMonitor")
public class HardUsageController {

    /**
     * 获取硬件使用率
     * @return
     */
    @GetMapping("/hardwareUsage")
    public List<String> hardwareUsage() {
        HardwareUsageService hardwareUsageService = new HardwareUsageService();
        System.out.println("Request hardwareUsage time = " + DateConverter.getTime());
        return hardwareUsageService.getAllUsage();
    }
}

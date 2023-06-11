package wang.ultra.my_utilities.common.monitor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.monitor.service.HardwareMonitorService;
import wang.ultra.my_utilities.hardware_monitor.service.MonitorService;

@RestController
@CrossOrigin
@RequestMapping("/monitor")
public class TestController {
    @Autowired
    HardwareMonitorService hardwareMonitorService;

    @GetMapping("insert")
    public void insert() {
//        hardwareMonitorService.randomRecord();

        MonitorService monitorService = new MonitorService();
        monitorService.monitor();


    }
}

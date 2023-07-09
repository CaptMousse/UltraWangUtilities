package wang.ultra.my_utilities.aliyun_ddns_update.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.aliyun_ddns_update.service.DdnsMonitorService;
import wang.ultra.my_utilities.aliyun_ddns_update.service.LogIOService;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/ddnsUpdate")
public class DDNSController {
    /**
     * 更新DDNS
     *
     * @return -1 - 更新出错, 0 - 无需更新, 1 - 更新成功
     */
    @GetMapping("/update")
    public AjaxUtils updateDDNS() {
        DdnsMonitorService.setNextUpdateTime(DateConverter.getTime(System.currentTimeMillis() + ConstantFromFile.getDdnsIntervalTime() * 3600 * 1000));
        return AjaxUtils.success(DdnsMonitorService.update());
    }

    /**
     * 初始化状态
     *
     * @return
     */
    @GetMapping("/getStatus")
    public AjaxUtils getStatus() {

        DdnsMonitorService.getStatus();

        List<String> list = new ArrayList<>();

        // 获取更新时间
        list.add(DdnsMonitorService.getNextUpdateTime());
        // 获取MC服务器地址
        list.add(ConstantFromFile.getHostname() + "." + ConstantFromFile.getDomain() + ":" + ConstantFromFile.getMinecraftPort());
        // 获取更新状态
        list.add(String.valueOf(DdnsMonitorService.getStatusResult()));
        // 获取上次更新时间
        list.add(LogIOService.getPreviousUpdateList(1).get(0));

//        System.out.println("getStatus time = " + DateConverter.getTime());

        return AjaxUtils.success(list);
    }

    /**
     * 初始化配置文件
     *
     * @return -1 = 失败, 1 = 成功
     */
    @GetMapping("/initConstFile")
    public AjaxUtils initConstFile() {

        Integer result = ConstantFromFile.setConstFromMap();

        DdnsMonitorService.setNextUpdateTime(DateConverter.getTime(System.currentTimeMillis() + ConstantFromFile.getDdnsIntervalTime() * 3600 * 1000));

        System.out.println("initConstFile time = " + DateConverter.getTime());

        return AjaxUtils.success(result);
    }

}

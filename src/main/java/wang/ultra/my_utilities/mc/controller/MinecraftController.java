package wang.ultra.my_utilities.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.mc.service.MinecraftService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/mc")
public class MinecraftController {

    @Autowired
    MinecraftService minecraftService;

    @GetMapping("blacklistSearchByName")
    public AjaxUtils blacklistSearchByName(String name) {
        Map<String, String> resultMap = minecraftService.blacklistSearchByName(name);
        if (!resultMap.isEmpty()) {
            Map<String, String> returnMap = new HashMap<>();
            String created = resultMap.get("created").substring(0, 10);
            returnMap.put("created", created);
            return AjaxUtils.success(returnMap);
        }
        return AjaxUtils.failed("没有查到哦~ ");

    }
    @GetMapping("whitelistSearchByName")
    public AjaxUtils whitelistSearchByName(String name) {
        Map<String, String> resultMap = minecraftService.whitelistSearchByName(name);
        if (!resultMap.isEmpty()) {
            return AjaxUtils.success("您已在白名单中! ", true);
        }
        return AjaxUtils.failed("您还没有在白名单, 请及时联系腐竹确认~ ");
    }

//    @GetMapping("whitelistAdd")
//    public AjaxUtils whitelistAdd(String name) {
//
//        String result = minecraftService.whitelistAdd(name);
//
//        if ("1".equals(result)) {
//            return AjaxUtils.success("白名单添加成功! ");
//        } else if ("-1".equals(result)) {
//            return AjaxUtils.failed("请检查白名单文件是否存在! ");
//        }
//
//        return AjaxUtils.failed(result);
//    }

//    @GetMapping("blacklistAdd")
//    public AjaxUtils blacklistAdd(String name, String reason) {
//
//        String result = minecraftService.blacklistAdd(name, reason);
//
//        if ("1".equals(result)) {
//            return AjaxUtils.success("黑名单添加成功! ");
//        } else if ("-1".equals(result)) {
//            return AjaxUtils.failed("请检查黑名单文件是否存在! ");
//        }
//
//        return AjaxUtils.failed(result);
//    }
}

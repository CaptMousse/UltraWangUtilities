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

    @GetMapping("bannedPlayerSearchByName")
    public AjaxUtils bannedPlayerSearchByName(String name) {
        System.out.println("name = " + name);

        Map<String, String> resultMap = minecraftService.bannedPlayersSearchByName(name);
        Map<String, String> returnMap = new HashMap<>();

        if (resultMap.isEmpty()) {
            String uuid = minecraftService.getBannedPlayerUUID(name);
            if (uuid != null) {
                resultMap = minecraftService.bannedPlayersSearchByUUID(uuid);
            }
        }

        if (!resultMap.isEmpty()) {
            String created = resultMap.get("created").substring(0, 10);
            returnMap.put("created", created);
            return AjaxUtils.success(returnMap);
        }
        return AjaxUtils.failed("没有查到哦~");

    }
}

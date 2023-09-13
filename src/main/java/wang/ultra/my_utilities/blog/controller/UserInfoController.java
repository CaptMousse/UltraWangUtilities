package wang.ultra.my_utilities.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.blog.service.UserPersonalInfoService;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/blog/user")
public class UserInfoController {

    @Autowired
    UserPersonalInfoService userPersonalInfoService;

    @GetMapping("/getUserInfo")
    public AjaxUtils getUserInfo(String username) {

        Map<String, String> userInfoMap = userPersonalInfoService.getUserInfo(username);

        return AjaxUtils.success(userInfoMap);

    }
}

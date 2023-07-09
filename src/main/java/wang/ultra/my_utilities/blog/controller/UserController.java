package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wang.ultra.my_utilities.blog.entity.UserLoginEntity;
import wang.ultra.my_utilities.blog.service.UserLoginInfoService;
import wang.ultra.my_utilities.blog.utils.UserLoginDecryptUtils;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/blog/user")
public class UserController {

    @Autowired
    UserLoginInfoService userLoginInfoService;

    @PostMapping("/login")
    public AjaxUtils login(String loginInfo, HttpSession session) {

        Map<String, String> loginInfoMap = UserLoginDecryptUtils.decryptUserInfoToMap(loginInfo);
        String username = loginInfoMap.get("username");
        String password = loginInfoMap.get("password");

        int result = loginCheck(username, password);

        switch (result) {
            case -2 -> {
                return AjaxUtils.failed("用户状态异常! ");
            }
            case -1 -> {
                return AjaxUtils.failed("用户名或密码错误! ");
            }
            case 0 -> {
                setSessionAttribute(session, username);
                return AjaxUtils.success("用户登陆成功! ");
            }
            default -> {
                return AjaxUtils.success("用户登陆失败! ");
            }
        }
    }

    /**
     * 验证用户名密码正确与否
     * @param username
     * @param password
     * @return  -1 用户名密码错误, -2状态错误
     */
    private int loginCheck(String username, String password) {

        Map<String, String> userMap = userLoginInfoService.userLoginSearchByUsername(username);
        if (userMap == null) {
            return -1;
        }
        if (!userMap.get("status").equals("0")) {
            return -2;
        }
        String salt = userMap.get("salt");
        String saltPasswordMD5 = StringUtils.makeMD5(password + salt);
        if (saltPasswordMD5.equals(userMap.get("password"))) {
            return 0;
        }

        return -1;
    }

    private void setSessionAttribute(HttpSession session, String username) {
        if (session != null) {
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(ConstantFromFile.getSessionInactiveInterval());
//            session.setAttribute("id", "1234567890");
        }
    }

    @PostMapping("/register")
    public AjaxUtils register(String userInfo) {

        Map<String, String> loginInfoMap = UserLoginDecryptUtils.decryptUserInfoToMap(userInfo);
        String username = loginInfoMap.get("username");
        String password = loginInfoMap.get("password");

        UserLoginEntity userLoginEntity = new UserLoginEntity(username, password);
        userLoginInfoService.userLoginInfoAdd(userLoginEntity);

        return AjaxUtils.success();
    }

    @GetMapping("/logout")
    public AjaxUtils logout(HttpSession session) {
        removeSessionAttribute(session);
        return AjaxUtils.success("登出成功!");
    }

    private void removeSessionAttribute(HttpSession session) {
        if (session != null) {
            session.removeAttribute("username");
//            session.removeAttribute("id");
        }
    }

    public AjaxUtils getSessionList(){
        return AjaxUtils.success();
    }
}

package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wang.ultra.my_utilities.blog.entity.UserLoginEntity;
import wang.ultra.my_utilities.blog.entity.UserLoginRecordEntity;
import wang.ultra.my_utilities.blog.service.UserLoginInfoService;
import wang.ultra.my_utilities.blog.service.UserLoginRecordService;
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
    @Autowired
    UserLoginRecordService userLoginRecordService;

    @PostMapping("/login")
    public AjaxUtils login(String loginInfo, HttpSession session, HttpServletRequest request) {

        Map<String, String> loginInfoMap = UserLoginDecryptUtils.decryptUserInfoToMap(loginInfo);
        String username = loginInfoMap.get("username");
        String password = loginInfoMap.get("password");



        Map<String, String> userMap = userLoginInfoService.userLoginSearchByUsername(username);
        if (userMap == null) {
            return AjaxUtils.failed("用户名或密码错误! ");
        }
        if (!userMap.get("status").equals("0")) {
            return AjaxUtils.failed("用户状态异常! ");
        }
        String salt = userMap.get("salt");
        String saltPasswordMD5 = StringUtils.makeMD5(password + salt);
        if (saltPasswordMD5.equals(userMap.get("password"))) {
            if (session != null) {
                // 登陆成功核心代码坐标
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(ConstantFromFile.getSessionInactiveInterval());

                userLoginRecordService.loginRecordAdd(new UserLoginRecordEntity(username, "用户登入", getCustomerIP(request)));


                return AjaxUtils.success("用户登陆成功! ");
            } else {
                return AjaxUtils.failed("用户登陆失败! ");
            }
        }
        return AjaxUtils.failed("用户名或密码错误! ");
    }

    /**
     * 获取客户端IP
     * @param request
     * @return
     */
    private String getCustomerIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
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
        if (session != null) {
            String username = (String) session.getAttribute("username");
            session.removeAttribute("username");

            userLoginRecordService.loginRecordAdd(new UserLoginRecordEntity(username, "用户登出"));

            return AjaxUtils.success("用户登出成功! ");
        } else {
            return AjaxUtils.failed("用户登出失败! ");
        }
    }
}

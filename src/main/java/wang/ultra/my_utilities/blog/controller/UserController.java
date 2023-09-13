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
import wang.ultra.my_utilities.common.cache.captcha.CaptchaCacheMap;
import wang.ultra.my_utilities.common.cache.username.UserLoginCacheMap;
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
    public AjaxUtils login(String loginInfo, String captchaId, String captcha, HttpSession session, HttpServletRequest request) {
        // 获取已保存的验证码
        if (captchaId == null || captchaId.isEmpty()) {
            return AjaxUtils.failed("验证码错误, 或已过期! ");
        }
        CaptchaCacheMap captchaCacheMap = new CaptchaCacheMap();
        String recordCaptcha = captchaCacheMap.getCaptcha(captchaId);
        captcha = captcha.toUpperCase();
        if (captcha.isEmpty()) {
            return AjaxUtils.failed("请输入验证码! ");
        } else if (!recordCaptcha.equals(captcha)) {
            return AjaxUtils.failed("验证码错误! ");
        }


        Map<String, String> loginInfoMap = UserLoginDecryptUtils.decryptUserInfoToMap(loginInfo);
        if (loginInfoMap == null || loginInfoMap.isEmpty()) {
            return AjaxUtils.failed("请输入用户名或密码! ");
        }
        String username = loginInfoMap.get("username");
        String password = loginInfoMap.get("password");


        Map<String, String> userMap = userLoginInfoService.userLoginSearchByUsername(username);
        if (userMap == null || userMap.isEmpty()) {
            return AjaxUtils.failed("用户名或密码错误! ");
        }
        if (!userMap.get("status").equals("0")) {
            return AjaxUtils.failed("用户状态异常! ");
        }
        String salt = userMap.get("salt");
        String saltPasswordMD5 = StringUtils.makeMD5(password + salt);
        if (saltPasswordMD5.equals(userMap.get("password"))) {
            // 使用用户map缓存替代session
            UserLoginCacheMap userLoginCacheMap = new UserLoginCacheMap();
            String loginToken = userLoginCacheMap.userLogin(username, getCustomerIP(request));
            if (loginToken != null) {
                return AjaxUtils.success("用户登陆成功! ", loginToken);
            }
        }
        return AjaxUtils.failed("用户名或密码错误! ");
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    private String getCustomerIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
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

            userLoginRecordService.logoutRecordAdd(new UserLoginRecordEntity(username, "用户登出"));

            return AjaxUtils.success("用户登出成功! ");
        } else {
            return AjaxUtils.failed("用户登出失败! ");
        }
    }

    @GetMapping("/ifLogin")
    public AjaxUtils ifLogin(String loginToken) {
        UserLoginCacheMap userLoginCacheMap = new UserLoginCacheMap();
        if (userLoginCacheMap.hasUsername(loginToken)) {
            return AjaxUtils.success(true);
        } else {
            return AjaxUtils.failed(false);
        }
    }
}

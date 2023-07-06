package wang.ultra.my_utilities.blog.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

@RestController
@CrossOrigin
@RequestMapping("/blog")
public class LoginController {

    @PostMapping("/login")
    public AjaxUtils login(String username, String password, HttpSession session) {

        if (username.equals("sefvdx") && password.equals("123456")) {
            setSessionAttribute(session, username);
            System.out.println("登陆成功! ");
            return AjaxUtils.success("登陆成功!");
        } else {
            System.out.println("登陆失败! ");
            return AjaxUtils.success("登陆失败!");
        }

    }

    private void setSessionAttribute(HttpSession session, String username) {
        if (session != null) {
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(ConstantFromFile.getSessionInactiveInterval());
//            session.setAttribute("id", "1234567890");
        }
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

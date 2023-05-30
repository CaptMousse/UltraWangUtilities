package wang.ultra.my_utilities.zbhd_scheduler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World!";
    }
    @GetMapping("/")
    public String test() {
        return "Test Hello World!";
    }
}

package wang.ultra.my_utilities.game2048;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/game2048")
public class Game2048Controller {

    Game2048Test game = new Game2048Test();

    @GetMapping("get")
    public AjaxUtils get() {
        Map<Integer, Integer> dashboard = game.getDashboard();
        return AjaxUtils.success(dashboard);
    }
    @GetMapping("init")
    public AjaxUtils init() {
        game.initDashboard();
        return AjaxUtils.success();
    }

    @GetMapping("start")
    public AjaxUtils start() {
        Map<Integer, Integer> dashboard = game.start();
        return AjaxUtils.success(dashboard);
    }

    @GetMapping("up")
    public AjaxUtils up() {
        Map<Integer, Integer> dashboard = game.actionUp();
        return AjaxUtils.success(dashboard);
    }
    @GetMapping("down")
    public AjaxUtils down() {
        Map<Integer, Integer> dashboard = game.actionDown();
        return AjaxUtils.success(dashboard);
    }
    @GetMapping("left")
    public AjaxUtils left() {
        Map<Integer, Integer> dashboard = game.actionLeft();
        return AjaxUtils.success(dashboard);
    }
    @GetMapping("right")
    public AjaxUtils right() {
        Map<Integer, Integer> dashboard = game.actionRight();
        return AjaxUtils.success(dashboard);
    }
}

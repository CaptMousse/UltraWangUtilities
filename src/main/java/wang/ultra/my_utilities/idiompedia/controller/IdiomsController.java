package wang.ultra.my_utilities.idiompedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.idiompedia.service.IdiomsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/idioms")
public class IdiomsController {

    @Autowired
    IdiomsService idiomsService;

    @GetMapping("/insert")
    public String insert() {
        long timeStart = System.currentTimeMillis();
        System.out.println("timeStart = " + timeStart);
        idiomsService.csvFile2Mapper("WabbyWabbo", "idiom.csv");
        long timeStop = System.currentTimeMillis();
        System.out.println("timeStop = " + timeStop);
        long timeTotal = timeStop - timeStart;

        String total;
        String timeTotalStr = String.valueOf(timeTotal);
        if(timeTotalStr.length() <= 3) {
            total = "timeTotal = " + timeTotalStr + "ms";
        } else {
            StringBuilder sb = new StringBuilder(timeTotalStr);
            sb.insert(timeTotalStr.length() - 3, ".");
            total = "timeTotal = " + sb + "s";
        }        
        System.out.println(total);

        return total;
    }

    @GetMapping("/selectByWord")
    public AjaxUtils selectByWord(String word) {
        Map<String, String> resultMap = idiomsService.idiomsSearchByWord(word);

        if (resultMap == null) {
            return AjaxUtils.failed("请输入成语!");
        }

        return AjaxUtils.success(resultMap);
    }

    @GetMapping("/selectBySort")
    public List<Map<String, Object>> selectBySort(String sort, String word) {
        return idiomsService.idiomsSearchBySort(Integer.parseInt(sort), word);
    }

    @GetMapping("/selectByQuestionMark")
    public AjaxUtils selectByQuestionMark(String word) {

        if (word.length() > 4) {
            return AjaxUtils.failed("仅限四字成语");
        }
        List<Map<String, Object>> returnList = idiomsService.idiomsSearchByQuestionMark(word);
        return AjaxUtils.success(returnList);
    }

    @GetMapping("/selectByRandom")
    public Map<String, String> selectByRandom(String word) {

        return idiomsService.idiomSearchByRandom();
    }

}
